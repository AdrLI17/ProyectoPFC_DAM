package com.ali.appjuegos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ali.appjuegos.databinding.FragmentNoSessionBinding
import com.ali.appjuegos.ui.auth.AuthActivity
import com.ali.appjuegos.ui.main.MainActivity

class NoSessionFragment : Fragment() {

    private var _binding: FragmentNoSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoSessionBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configurar el mensaje
        binding.textViewMessage.text = getString(R.string.no_session_message)

        // Asignar clic al botón de iniciar sesión
        binding.btnLogin.setOnClickListener {
            // Aquí manejas la acción al hacer clic en el botón de iniciar sesión
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }

        // Asignar clic al botón de registrarse
        binding.btnRegister.setOnClickListener {
            // Aquí manejas la acción al hacer clic en el botón de registrarse
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
