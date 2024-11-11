package ru.slavapmk.truthoraction.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.dto.game.Player
import ru.slavapmk.truthoraction.dto.game.Players

class PlayerAdapter(
    private val myDataset: Players,
    private val callback: (Player) -> (Unit)
) : RecyclerView.Adapter<PlayerAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val index: TextView = itemView.findViewById(R.id.index)
        val name: TextView = itemView.findViewById(R.id.name)
        val button: TextView = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.player_item, parent, false)
        return NotificationViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val player = myDataset.players[position]
        holder.index.text = (position + 1).toString()
        holder.name.text = player.name
        holder.button.setOnClickListener {
            Log.d("gerfwdq", "gefrwsxq")
            callback(player)
        }
    }

    override fun getItemCount() = myDataset.players.size
}