package ru.slavapmk.truthoraction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.databinding.FragmentGameBinding
import ru.slavapmk.truthoraction.databinding.FragmentPlayersBinding
import ru.slavapmk.truthoraction.dto.game.Player
import ru.slavapmk.truthoraction.dto.game.Players

class PlayersFragment : Fragment() {
    private lateinit var binding: FragmentPlayersBinding
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private lateinit var players: Players
    private lateinit var playersAdapter: PlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentPlayersBinding.inflate(layoutInflater)

        players = getPlayers()
        playersAdapter = PlayerAdapter(
            players
        ) { player: Player ->
            removePlayer(player)
        }

        binding.playersList.adapter = playersAdapter
        binding.playersList.layoutManager = LinearLayoutManager(context)

        binding.adduser.setEndIconOnClickListener {
            if (binding.adduserEdit.text?.isNotEmpty() == true) {
                players.players.add(
                    Player(binding.adduserEdit.text.toString())
                )
                binding.adduserEdit.text?.clear()
                updatePlayers(players)
                playersAdapter.notifyItemInserted(players.players.size)
            }
        }

        return binding.root
    }

    fun removePlayer(player: Player) {
        val index = players.players.indexOf(player)
        players.players.removeAt(index)
        updatePlayers(players)
        playersAdapter.notifyItemRemoved(index)
    }

    fun getPlayers() = activity.playersCodec.decodePlayers(
        activity.shared.getString(
            "players",
            activity.playersCodec.encodePlayers(Players())
        )!!
    )

    fun updatePlayers(players: Players) = activity.shared.edit {
        players.current = 0
        putString(
            "players",
            activity.playersCodec.encodePlayers(players)
        )
        commit()
    }
}