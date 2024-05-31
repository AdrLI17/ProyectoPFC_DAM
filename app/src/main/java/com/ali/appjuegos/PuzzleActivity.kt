package com.ali.appjuegos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.ali.appjuegos.data.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

// Datos de una pieza del puzzle
data class PuzzlePiece(val originalPosition: Int, var currentPosition: Int, val bitmap: Bitmap)

class PuzzleActivity : AppCompatActivity() {

    private lateinit var puzzlePieces: List<PuzzlePiece>
    private lateinit var originalBitmap: Bitmap
    private val gridSize = 3
    private val flagApiBaseUrl = "https://picsum.photos/200/200"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        // Obtener imagen desde una URL
        fetchImage(flagApiBaseUrl)

        // Botón para mezclar el puzzle
        findViewById<Button>(R.id.btn_shuffle).setOnClickListener {
            shufflePuzzle()
        }

        // Botón para reiniciar el puzzle
        findViewById<Button>(R.id.btn_restart).setOnClickListener {
            restartPuzzle()
        }

        // Botón para validar si el puzzle está completo
        findViewById<Button>(R.id.btn_validate).setOnClickListener {
            checkIfPuzzleIsComplete()
        }
    }

    // Función para obtener una imagen desde una URL
    private fun fetchImage(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = URL(url).openStream()
                originalBitmap = BitmapFactory.decodeStream(inputStream)
                Log.d("PuzzleActivity", "Imagen obtenida con éxito")
                withContext(Dispatchers.Main) {
                    initializePuzzle()
                }
            } catch (e: Exception) {
                Log.e("PuzzleActivity", "Error al obtener la imagen: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PuzzleActivity, "Error al obtener la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Inicializar el puzzle
    private fun initializePuzzle() {
        puzzlePieces = splitImage(originalBitmap, gridSize, gridSize)
        val puzzleContainer = findViewById<FrameLayout>(R.id.puzzle_container)
        puzzleContainer.removeAllViews()

        val pieceSize = puzzleContainer.width / gridSize

        for (piece in puzzlePieces) {
            val imageView = ImageView(this).apply {
                setImageBitmap(piece.bitmap)
                tag = piece
                layoutParams = FrameLayout.LayoutParams(pieceSize, pieceSize).apply {
                    leftMargin = (piece.originalPosition % gridSize) * pieceSize
                    topMargin = (piece.originalPosition / gridSize) * pieceSize
                }
                setOnTouchListener(PieceTouchListener())
            }
            puzzleContainer.addView(imageView)
        }
    }

    // Dividir la imagen en piezas
    private fun splitImage(image: Bitmap, rows: Int, cols: Int): List<PuzzlePiece> {
        val pieces = mutableListOf<PuzzlePiece>()
        val pieceWidth = image.width / cols
        val pieceHeight = image.height / rows

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = col * pieceWidth
                val y = row * pieceHeight
                val pieceBitmap = Bitmap.createBitmap(image, x, y, pieceWidth, pieceHeight)
                pieces.add(PuzzlePiece(row * cols + col, row * cols + col, pieceBitmap))
            }
        }

        pieces.shuffle()
        return pieces
    }

    // Manejar el toque de las piezas del puzzle
    private inner class PieceTouchListener : View.OnTouchListener {
        private var downX: Float = 0f
        private var downY: Float = 0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = view.x + (event.x - downX)
                    val newY = view.y + (event.y - downY)
                    view.x = newX
                    view.y = newY
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val piece = view.tag as PuzzlePiece
                    val newPosition = checkPiecePosition(view, piece)
                    if (newPosition != piece.currentPosition) {
                        piece.currentPosition = newPosition
                    }
                    return true
                }
            }
            return false
        }
    }

    // Verificar la posición de la pieza del puzzle
    private fun checkPiecePosition(view: View, piece: PuzzlePiece): Int {
        val container = findViewById<FrameLayout>(R.id.puzzle_container)
        val pieceSize = container.width / gridSize

        val row = (view.y / pieceSize).toInt()
        val col = (view.x / pieceSize).toInt()
        val position = row * gridSize + col

        if (position in 0 until gridSize * gridSize) {
            val originalRow = piece.originalPosition / gridSize
            val originalCol = piece.originalPosition % gridSize

            // Verificar si la pieza está dentro de un rango aceptable alrededor de su posición original
            val tolerance = pieceSize / 2 // Se puede ajustar según la sensibilidad deseada
            val isApproximatelyInPlace =
                row in (originalRow - 1)..(originalRow + 1) &&
                        col in (originalCol - 1)..(originalCol + 1)

            if (isApproximatelyInPlace) {
                return piece.originalPosition // Devolver la posición original si está aproximadamente en su lugar
            }
        }

        return piece.currentPosition
    }

    // Comprobar si el puzzle está completo
    private fun checkIfPuzzleIsComplete() {
        if (puzzlePieces.all { it.currentPosition == it.originalPosition }) {
            // Verificar si hay una sesión iniciada
            if (LocalDatabase.userData != null) {
                // Sumar 50 plátanos al usuario actual
                LocalDatabase.addPoints(50)
                Toast.makeText(this, "¡Puzzle completo! +50 plátanos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "¡Puzzle completo!", Toast.LENGTH_SHORT).show()
            }
            // Cargar una nueva imagen mezclada
            loadNewImage()
        } else {
            Toast.makeText(this, "Puzzle incompleto. ¡Sigue intentando!", Toast.LENGTH_SHORT).show()
        }
    }

    // Mezclar el puzzle
    private fun shufflePuzzle() {
        puzzlePieces = puzzlePieces.shuffled()
        val puzzleContainer = findViewById<FrameLayout>(R.id.puzzle_container)
        puzzleContainer.children.forEachIndexed { index, view ->
            val piece = puzzlePieces[index]
            piece.currentPosition = index
            val pieceSize = puzzleContainer.width / gridSize
            view.layoutParams = FrameLayout.LayoutParams(pieceSize, pieceSize).apply {
                leftMargin = (index % gridSize) * pieceSize
                topMargin = (index / gridSize) * pieceSize
            }
        }
    }

    // Cargar una nueva imagen
    private fun loadNewImage() {
        fetchImage(flagApiBaseUrl)
    }

    // Reiniciar el puzzle
    private fun restartPuzzle() {
        initializePuzzle()
    }

    // Mostrar mensaje de error
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
