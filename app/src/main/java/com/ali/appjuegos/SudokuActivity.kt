package com.ali.appjuegos

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ali.appjuegos.databinding.ActivitySudokuBinding
import kotlin.random.Random

class SudokuActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySudokuBinding
    private lateinit var sudoku: Sudoku
    private lateinit var sudokuGrid: Array<Array<EditText>>
    private lateinit var selectedValue: String
    private var selectedRow = 0
    private var selectedColumn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySudokuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sudoku = Sudoku()

        // Generar un Sudoku válido y llenar la cuadrícula en el XML
        val board = sudoku.getGrid()
        fillSudokuGrid(board)

        // Inicializar la cuadrícula de Sudoku
        sudokuGrid = Array(9) { row ->
            Array(9) { col ->
                val cellId = resources.getIdentifier("a_${row}${col}", "id", packageName)
                findViewById(cellId)
            }
        }

        // Agregar listeners a cada celda de la cuadrícula
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = sudokuGrid[i][j]
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        val resourceId = editText.id
                        val resourceName = resources.getResourceName(resourceId)
                        val resourceNameParts = resourceName.split("/")
                        val resourceNameWithoutPrefix = resourceNameParts[1]
                        selectedRow = resourceNameWithoutPrefix.substring(2, 3).toInt()
                        selectedColumn = resourceNameWithoutPrefix.substring(3, 4).toInt()
                        selectedValue = s.toString()
                        Log.d("TEST", "Value: $selectedValue, Row: $selectedRow, Column: $selectedColumn, Name: $resourceNameWithoutPrefix, ID: $resourceId")
                    }
                })
                editText.setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        val resourceId = editText.id
                        val resourceName = resources.getResourceName(resourceId)
                        val resourceNameParts = resourceName.split("/")
                        val resourceNameWithoutPrefix = resourceNameParts[1]
                        selectedRow = resourceNameWithoutPrefix.substring(2, 3).toInt()
                        selectedColumn = resourceNameWithoutPrefix.substring(3, 4).toInt()
                        selectedValue = editText.text.toString()
                        Log.d("TEST", "Value: $selectedValue, Row: $selectedRow, Column: $selectedColumn, Name: $resourceNameWithoutPrefix, ID: $resourceId")
                    }
                }
            }
        }

        // Listener del botón "SOLVE"
        binding.btnSolve.setOnClickListener {
            // Verificar la solución del Sudoku
            checkSolution(selectedRow, selectedColumn, selectedValue.toInt())
        }

        // Listener del botón "CLEAR" (Aún no implementado)
        binding.btnClear.setOnClickListener {
            // Implementar lógica para limpiar el tablero
            clearSudokuGrid()
            Toast.makeText(this, "Tablero limpiado", Toast.LENGTH_SHORT).show()
        }
    }

    // Llenar la cuadrícula de Sudoku con el tablero generado
    private fun fillSudokuGrid(board: Array<IntArray>) {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = findViewById<EditText>(resources.getIdentifier("a_${i}${j}", "id", packageName))
                editText.setText(board[i][j].toString())

                // Bloquear las casillas que no están vacías
                if (board[i][j] != 0) {
                    editText.isEnabled = false
                } else {
                    editText.text.clear()
                }
            }
        }
    }

    // Verificar la solución del Sudoku
    private fun checkSolution(row: Int, col: Int, userInput: Int) {
        val solutionValue = sudoku.getSolution()[row][col]
        if (userInput == solutionValue) {
            // Si el número ingresado es correcto, establecer el color del EditText en verde
            sudokuGrid[row][col].setTextColor(Color.GREEN)
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
        } else {
            // Si el número ingresado es incorrecto, establecer el color del EditText en rojo
            sudokuGrid[row][col].setTextColor(Color.RED)
            Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show()
        }
    }

    // Limpiar la cuadrícula de Sudoku
    private fun clearSudokuGrid() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = sudokuGrid[i][j]
                if (editText.isEnabled) {
                    editText.text.clear()
                    editText.setTextColor(Color.BLACK)
                }
            }
        }
    }
}

// Clase para generar y resolver Sudoku
class Sudoku {
    private val grid = Array(9) { IntArray(9) }
    private lateinit var solution: Array<IntArray>

    init {
        generateSudoku()
    }

    fun getGrid(): Array<IntArray> {
        return grid
    }

    fun getSolution(): Array<IntArray> {
        return solution
    }

    private fun generateSudoku() {
        // Lógica para generar un Sudoku válido
        solveSudoku(0, 0)
        solution = grid.map { it.copyOf() }.toTypedArray()
        removeNumbers()
    }

    private fun solveSudoku(row: Int, col: Int): Boolean {
        if (row == 9) return true

        var nextRow = row
        var nextCol = col + 1
        if (nextCol == 9) {
            nextRow++
            nextCol = 0
        }

        if (grid[row][col] != 0) {
            return solveSudoku(nextRow, nextCol)
        }

        val numbers = (1..9).shuffled()
        for (num in numbers) {
            if (isValid(row, col, num)) {
                grid[row][col] = num
                if (solveSudoku(nextRow, nextCol)) {
                    return true
                }
                grid[row][col] = 0
            }
        }
        return false
    }

    private fun isValid(row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 9) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false
            }
        }

        val startRow = row - row % 3
        val startCol = col - col % 3
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                if (grid[i][j] == num) {
                    return false
                }
            }
        }

        return true
    }

    private fun removeNumbers() {
        val totalCells = 81
        val emptyCells = totalCells * 2 / 3 // Dejar aproximadamente 2/3 de las celdas vacías
        var removed = 0

        while (removed < emptyCells) {
            val row = Random.nextInt(9)
            val col = Random.nextInt(9)
            if (grid[row][col] != 0) {
                grid[row][col] = 0
                removed++
            }
        }
    }

    fun printSudoku() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                print("${grid[i][j]} ")
            }
            println()
        }
    }
}
