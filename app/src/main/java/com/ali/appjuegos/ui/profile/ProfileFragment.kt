package com.ali.appjuegos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ali.appjuegos.data.LocalDatabase
import com.ali.appjuegos.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userData = LocalDatabase.userData

        binding.textViewUsername.text = userData?.username ?: "Invitado"
        binding.textViewSudokuCompleted.text = "Sudokus completados: ${userData?.sudokuCompleted ?: 0}"
        binding.textViewPuzzleCompleted.text = "Rompecabezas completados: ${userData?.puzzleCompleted ?: 0}"
        binding.textViewMemoryCompleted.text = "Memory completados: ${userData?.memoryCompleted ?: 0}"
        binding.textViewWordSearchCompleted.text = "Sopa de letras completadas: ${userData?.wordSearchCompleted ?: 0}"

        binding.btnEditProfile.setOnClickListener {
            // Lógica para editar el perfil
        }

        binding.btnMoreOptions.setOnClickListener {
            // Lógica para opciones adicionales
        }
    }
}
