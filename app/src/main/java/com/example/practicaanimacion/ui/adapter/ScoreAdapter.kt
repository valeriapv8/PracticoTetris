package com.example.practicaanimacion.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaanimacion.databinding.ItemScoreBinding
import com.example.practicaanimacion.db.dao.ScoreEntity

class ScoreAdapter(private val scores: List<ScoreEntity>) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {
    class ScoreViewHolder(val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val item = scores[position]
        holder.binding.tvPlayerName.text = item.playerName
        holder.binding.tvPlayerScore.text = item.score.toString()
    }

    override fun getItemCount(): Int = scores.size
}