﻿package ru.slavapmk.truthoraction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.slavapmk.truthoraction.databinding.FragmentGameBinding
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.dto.game.History
import ru.slavapmk.truthoraction.dto.game.Players
import ru.slavapmk.truthoraction.io.GenerateResult
import kotlin.random.Random

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private var answering = AnswerType.NONE
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private var current: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentGameBinding.inflate(layoutInflater)

        binding.truthAction.setOnClickListener {
            generateTruth()
        }

        binding.actionAction.setOnClickListener {
            generateAction()
        }

        binding.randomAction.setOnClickListener {
            when (Random.nextBoolean()) {
                true -> generateTruth()
                false -> generateAction()
            }
        }

        binding.actionRoll.setOnClickListener {
            when (answering) {
                AnswerType.NONE -> {}
                AnswerType.TRUTH -> generateTruth()
                AnswerType.ACTION -> generateAction()
            }
        }

        binding.actionNext.setOnClickListener {
            val history = activity.historyCodec.decodeHistory(
                activity.shared.getString(
                    "history", activity.historyCodec.encodeHistory(History())
                )!!
            )
            when (answering) {
                AnswerType.NONE -> throw IllegalStateException()
                AnswerType.ACTION -> history.actions
                AnswerType.TRUTH -> history.truths
            }.apply {
                if (current != null) {
                    add(0, current!!)
                    if (size > 500) {
                        for (i in 500 until size) {
                            removeAt(i)
                        }
                    }
                }
            }
            activity.shared.edit {
                putString(
                    "history",
                    activity.historyCodec.encodeHistory(history)
                )
                commit()
            }

            val players = getPlayers()
            players.current = (players.current + 1) % (players.players.size)
            updatePlayers(players)

            updateAnswering(AnswerType.NONE)
        }

        updateAnswering(AnswerType.NONE)

        return binding.root
    }

    private fun updateAnswering(status: AnswerType) {
        answering = status
        when (answering) {
            AnswerType.ACTION, AnswerType.TRUTH -> {
                binding.actionNext.visibility = View.VISIBLE
                binding.actionRoll.visibility = View.VISIBLE

                binding.actionAction.visibility = View.INVISIBLE
                binding.truthAction.visibility = View.INVISIBLE
                binding.randomAction.visibility = View.INVISIBLE
            }

            AnswerType.NONE -> {
                binding.actionNext.visibility = View.INVISIBLE
                binding.actionRoll.visibility = View.INVISIBLE

                binding.actionAction.visibility = View.VISIBLE
                binding.truthAction.visibility = View.VISIBLE
                binding.randomAction.visibility = View.VISIBLE

                binding.taskType.text = ""
                val players = getPlayers()
                binding.question.text =
                    getString(R.string.queue_of, players.players[players.current].name)
                binding.name.text =
                    getString(R.string.name, players.players[players.current].name)
                binding.round.text = getString(
                    R.string.round,
                    players.current + 1,
                    players.players.size
                )
            }
        }
    }

    private fun generateTruth() {
        updateAnswering(AnswerType.TRUTH)
        binding.taskType.text = "Правда"
        binding.question.text = ""
        binding.generationProgress.isVisible = true

        val aiGameInteractor = activity.aiGameInteractor

        binding.actionRoll.isClickable = false
        binding.actionNext.isClickable = false
        CoroutineScope(Dispatchers.IO).launch {
            val players = getPlayers()
            val result = aiGameInteractor.generateTruth(
                players.players.map { it.name },
                players.players[players.current].name,
                activity.shared.getString("aiSettings", "")!!,
                activity.historyCodec.decodeHistory(
                    activity.shared.getString(
                        "history", activity.historyCodec.encodeHistory(History())
                    )!!
                )
            )

            withContext(Dispatchers.Main) {
                binding.question.text = when (result) {
                    GenerateResult.IllegalRegion -> getString(R.string.illegal_region)
                    GenerateResult.ParseError -> getString(R.string.parse_error)
                    GenerateResult.QuotaLimit -> getString(R.string.quota_limit)
                    is GenerateResult.HttpError -> getString(R.string.http_error, result.code)
                    is GenerateResult.Success -> {
                        current = result.text
                        result.text
                    }
                }
                binding.generationProgress.isVisible = false
                binding.actionRoll.isClickable = true
                binding.actionNext.isClickable = true
            }
        }
    }

    private fun generateAction() {
        updateAnswering(AnswerType.ACTION)
        binding.taskType.text = "Действие"
        binding.question.text = ""
        binding.generationProgress.isVisible = true

        val aiGameInteractor = activity.aiGameInteractor

        binding.actionRoll.isClickable = false
        binding.actionNext.isClickable = false
        CoroutineScope(Dispatchers.IO).launch {
            val players = getPlayers()
            val result = aiGameInteractor.generateAction(
                players.players.map { it.name },
                players.players[players.current].name,
                activity.shared.getString("aiSettings", "")!!,
                activity.historyCodec.decodeHistory(
                    activity.shared.getString(
                        "history", activity.historyCodec.encodeHistory(History())
                    )!!
                )
            )

            withContext(Dispatchers.Main) {
                binding.question.text = when (result) {
                    GenerateResult.IllegalRegion -> getString(R.string.illegal_region)
                    GenerateResult.ParseError -> getString(R.string.parse_error)
                    GenerateResult.QuotaLimit -> getString(R.string.quota_limit)
                    is GenerateResult.HttpError -> getString(R.string.http_error, result.code)
                    is GenerateResult.Success -> {
                        current = result.text
                        result.text
                    }
                }
                binding.generationProgress.isVisible = false
                binding.actionRoll.isClickable = true
                binding.actionNext.isClickable = true
            }
        }
    }

    fun getPlayers() = activity.playersCodec.decodePlayers(
        activity.shared.getString(
            "players",
            activity.playersCodec.encodePlayers(Players())
        )!!
    )

    fun updatePlayers(players: Players) = activity.shared.edit {
        putString(
            "players",
            activity.playersCodec.encodePlayers(players)
        )
        commit()
    }
}

enum class AnswerType {
    NONE, TRUTH, ACTION
}