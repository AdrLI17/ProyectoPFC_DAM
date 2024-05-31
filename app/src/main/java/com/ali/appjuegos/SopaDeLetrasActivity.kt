package com.ali.appjuegos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ali.appjuegos.data.LocalDatabase
import kotlin.random.Random

class SopaDeLetrasActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var wordInput: EditText
    private lateinit var submitButton: Button

    // Palabras para el juego de sopa de letras
    private val words = arrayOf(
        "LATA", "CASA", "PERA", "MESA", "CART", "CAMA", "LUNA", "SOLA", "RATA",
        "AMOR", "VINO", "AVIS", "FOTO", "PATO", "LEON", "RICO", "LAGO", "VELA",
        "NUBE", "OSO", "VACA", "RIO", "VIDA", "FRIO", "SALA", "DADO", "GATO",
        "RAZA", "POLO", "MOTO", "LODO", "TAPA", "NATA", "NATO", "BOLA", "PELO",
        "TORT", "LISA", "RANA", "PALA"
    )
    private val gridWidth = 8
    private val gridHeight = 8
    private val gridSize = gridWidth * gridHeight
    private lateinit var letters: Array<String>
    private lateinit var targetWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sopa_de_letras)

        // Inicializar las vistas
        gridView = findViewById(R.id.gridView)
        wordInput = findViewById(R.id.wordInput)
        submitButton = findViewById(R.id.submitButton)

        // Inicializar la cuadrícula y la entrada de palabra
        initializeGridView()
        initializeWordInput()
    }

    // Inicializar la vista de la cuadrícula
    private fun initializeGridView() {
        targetWord = words.random()

        // Generar la cuadrícula con letras aleatorias
        letters = generateRandomLetters(gridSize, targetWord)

        // Mostrar la cuadrícula en la vista
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, letters)
        gridView.adapter = adapter
    }

    // Generar letras aleatorias y colocar la palabra objetivo en la cuadrícula
    private fun generateRandomLetters(count: Int, word: String): Array<String> {
        val letters = mutableListOf<String>()

        val positionsGroup1 = listOf(18, 19, 20, 21)
        val positionsGroup2 = listOf(40, 41, 42, 43)
        val positionsGroup3 = listOf(63, 55, 47, 39)
        val positionsGroup4 = listOf(4, 5, 6, 7)
        val positionsGroup5 = listOf(32, 24, 16, 8)
        val positionsGroup6 = listOf(59, 60, 61, 62)

        // Lista con todos los grupos de posiciones
        val allPositionsGroups = listOf(positionsGroup1, positionsGroup2, positionsGroup3, positionsGroup4, positionsGroup5, positionsGroup6)

        // Elegir aleatoriamente uno de los grupos de posiciones
        val chosenPositions = allPositionsGroups.random()

        // Rellenar las posiciones de la palabra objetivo
        for (i in 0 until count) {
            if (i in chosenPositions) {
                letters.add(targetWord[chosenPositions.indexOf(i)].toString())
            } else {
                letters.add(('A'..'Z').random().toString())
            }
        }

        return letters.toTypedArray()
    }

    // Inicializar la entrada de palabra y el botón de envío
    private fun initializeWordInput() {
        submitButton.setOnClickListener {
            val userWord = wordInput.text.toString()
            // Verificar si la palabra ingresada es correcta
            if (userWord == targetWord) {
                if (LocalDatabase.userData != null) {
                    // Sumar 50 plátanos al usuario actual
                    LocalDatabase.addPoints(50)
                    Toast.makeText(this, "¡Palabra encontrada! +50 plátanos", Toast.LENGTH_SHORT).show()
                    initializeGridView() // Generar una nueva sopa de letras con una nueva palabra
                } else {
                    Toast.makeText(this, "¡Palabra encontrada!", Toast.LENGTH_SHORT).show()
                    initializeGridView() // Generar una nueva sopa de letras con una nueva palabra
                }
            } else {
                Toast.makeText(this, "Palabra incorrecta, sigue buscando", Toast.LENGTH_SHORT).show()
            }
            wordInput.text.clear()
        }
    }
}
