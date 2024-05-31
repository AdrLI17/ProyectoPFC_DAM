package com.ali.appjuegos


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ali.appjuegos.data.LocalDatabase
import com.ali.appjuegos.databinding.FragmentInicioBinding
import com.ali.appjuegos.ui.auth.AuthActivity

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.root

            // Asignación de clics a los botones de juegos
            binding.btnSudoku.setOnClickListener {
                startSudoku()
            }

            binding.btnRompecabezas.setOnClickListener {
                startPuzzle()
            }

            binding.btnMemory.setOnClickListener {
                startMemory()
            }

            binding.btnSopaLetras.setOnClickListener {
                startSopaDeLetras()
            }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startSudoku() {
        // Lanzar la actividad del juego de Sudoku
        val intent = Intent(requireContext(), SudokuActivity::class.java)
        startActivity(intent)
    }

    private fun startPuzzle() {
        // Lanzar la actividad del juego de Puzzle
        val intent = Intent(requireContext(), PuzzleActivity::class.java)
        startActivity(intent)
    }

    private fun startSopaDeLetras() {
        // Lanzar la actividad del juego de Puzzle
        val intent = Intent(requireContext(), SopaDeLetrasActivity::class.java)
        startActivity(intent)
    }

    private fun startMemory() {
        // Lanzar la actividad del juego de Puzzle
        val intent = Intent(requireContext(), MemoryActivity::class.java)
        startActivity(intent)
    }

}
