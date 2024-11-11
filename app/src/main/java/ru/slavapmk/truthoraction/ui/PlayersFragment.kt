package ru.slavapmk.truthoraction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
            addPeople()
        }

        binding.adduserEdit.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                addPeople()
                true
            } else {
                false
            }
        }

        return binding.root
    }

    private fun addPeople() {
        if (binding.adduserEdit.text?.isNotEmpty() == true) {
            players.players.add(
                Player(binding.adduserEdit.text.toString())
            )
            binding.adduserEdit.text?.clear()
            updatePlayers(players)
            playersAdapter.notifyItemInserted(players.players.size)
        }
    }

    private fun removePlayer(player: Player) {
        val index = players.players.indexOf(player)
        players.players.removeAt(index)
        updatePlayers(players)
        playersAdapter.notifyItemRemoved(index)
    }

    private fun getPlayers() = activity.playersCodec.decodePlayers(
        activity.shared.getString(
            "players",
            activity.playersCodec.encodePlayers(Players())
        )!!
    )

    private fun updatePlayers(players: Players) = activity.shared.edit {
        players.current = 0
        activity.shared.edit {
            putString("answer_type", "none")
            putString("current", null)
            commit()
        }
        putString(
            "players",
            activity.playersCodec.encodePlayers(players)
        )
        commit()
    }
}