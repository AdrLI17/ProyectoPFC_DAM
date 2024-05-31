package com.ali.appjuegos.ui.rankings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ali.appjuegos.R
import com.ali.appjuegos.model.RankingEntry

class RankingAdapter(private val rankings: List<RankingEntry>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewUsername)
        val pointsTextView: TextView = itemView.findViewById(R.id.textViewPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val rankingEntry = rankings[position]
        holder.usernameTextView.text = rankingEntry.username
        holder.pointsTextView.text = "${rankingEntry.points} 🍌" // Plátanos como icono de puntos
    }

    override fun getItemCount(): Int {
        return rankings.size
    }
}
