package com.ali.appjuegos

import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ali.appjuegos.data.LocalDatabase

class MemoryActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var pairsFoundTextView: TextView
    private val numColumns = 4
    private val numPairs = 8 // 4x4 grid -> 8 pairs
    private val cards = mutableListOf<String>()
    private val flippedCards = mutableListOf<Int>()
    private val matchedCards = mutableSetOf<Int>()
    private var firstCardIndex: Int? = null
    private var secondCardIndex: Int? = null
    private var pairsFound = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)

        gridView = findViewById(R.id.gridView)
        pairsFoundTextView = findViewById(R.id.pairsFoundTextView)

        // Inicializar las cartas y la vista de la cuadrícula
        initializeCards()
        initializeGridView()
        updatePairsFoundTextView()
    }

    // Inicializar las cartas con pares de números como cadenas y mezclarlas
    private fun initializeCards() {
        val pairs = List(numPairs) { (it + 1).toString() }
        cards.addAll(pairs)
        cards.addAll(pairs)
        cards.shuffle()
    }

    // Inicializar el GridView y configurar el adaptador
    private fun initializeGridView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cards.map { "?" })
        gridView.adapter = adapter
        gridView.numColumns = numColumns
        gridView.setOnItemClickListener { _, _, position, _ ->
            onCardClick(position)
        }
    }

    // Manejar el evento de clic en una carta
    private fun onCardClick(position: Int) {
        // Ignorar si la carta ya está emparejada o volteada
        if (matchedCards.contains(position) || flippedCards.contains(position)) {
            return
        }

        flippedCards.add(position)
        updateGridView()

        // Verificar si hay dos cartas volteadas
        if (flippedCards.size == 2) {
            firstCardIndex = flippedCards[0]
            secondCardIndex = flippedCards[1]

            // Si las cartas coinciden
            if (cards[firstCardIndex!!] == cards[secondCardIndex!!]) {
                matchedCards.add(firstCardIndex!!)
                matchedCards.add(secondCardIndex!!)
                flippedCards.clear()
                pairsFound++
                updatePairsFoundTextView()
                // Si se han encontrado todas las parejas
                if (matchedCards.size == cards.size) {
                    onComplete()
                }
            } else {
                // Si las cartas no coinciden, voltearlas nuevamente después de un retraso
                handler.postDelayed({
                    flippedCards.clear()
                    updateGridView()
                }, 1000)
            }
        }

        updateGridView()
    }

    // Actualizar el GridView para mostrar las cartas volteadas y emparejadas
    private fun updateGridView() {
        val displayCards = cards.mapIndexed { index, card ->
            if (flippedCards.contains(index) || matchedCards.contains(index)) {
                card
            } else {
                "?"
            }
        }
        adapter.clear()
        adapter.addAll(displayCards)
        adapter.notifyDataSetChanged()
    }

    // Actualizar la vista del contador de parejas encontradas
    private fun updatePairsFoundTextView() {
        pairsFoundTextView.text = "Parejas encontradas: $pairsFound / $numPairs"
    }

    // Manejar la finalización del juego
    private fun onComplete() {
        if (LocalDatabase.userData != null) {
            // Sumar 50 plátanos al usuario actual
            LocalDatabase.addPoints(50)
            Toast.makeText(this, "¡Puzzle completado! +50 plátanos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "¡Puzzle completado!", Toast.LENGTH_SHORT).show()
        }

        // Reiniciar el juego
        resetGame()
    }

    // Reiniciar el juego
    private fun resetGame() {
        matchedCards.clear()
        pairsFound = 0
        initializeCards()
        updateGridView()
        updatePairsFoundTextView()
    }
}
