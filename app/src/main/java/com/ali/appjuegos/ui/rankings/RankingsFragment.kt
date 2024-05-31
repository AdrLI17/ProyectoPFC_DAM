package com.ali.appjuegos.ui.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali.appjuegos.data.LocalDatabase
import com.ali.appjuegos.databinding.FragmentRankingsBinding
import com.ali.appjuegos.model.RankingEntry

class RankingsFragment : Fragment() {

    private var _binding: FragmentRankingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingsBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener todos los usuarios
        val users = LocalDatabase.getAllUsers()

        // Crear la lista de rankings con los datos de todos los usuarios
        val rankings = users.map { user ->
            RankingEntry(user.username, user.points)
        }

        // Crear un adaptador para el RecyclerView
        val adapter = RankingAdapter(rankings)

        // Asignar el adaptador al RecyclerView
        binding.recyclerViewRankings.adapter = adapter

        // Configurar el LayoutManager (LinearLayoutManager en este caso)
        binding.recyclerViewRankings.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
