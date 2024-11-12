package ru.slavapmk.truthoraction.ui

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.dto.game.Player
import ru.slavapmk.truthoraction.dto.game.Players

class PlayerAdapter(
    private val myDataset: Players,
    private val callback: (Player) -> (Unit)
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val index: TextView = itemView.findViewById(R.id.index)
        val name: TextView = itemView.findViewById(R.id.name)
        val button: TextView = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.player_item, parent, false)
        return PlayerViewHolder(parent.context, itemView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = myDataset.players[position]
        holder.index.text = holder.context.getString(
            R.string.player_index, position + 1
        )
        holder.name.text = player.name
        holder.button.setOnClickListener {
            holder.button.isClickable = false
            callback(player)
        }
    }

    override fun getItemCount() = myDataset.players.size
}