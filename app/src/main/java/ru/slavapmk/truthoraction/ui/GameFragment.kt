package ru.slavapmk.truthoraction.ui

import android.os.Bundle
import android.text.Html
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
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private var current: String?
        get() = activity.shared.getString("current", null)
        set(new) = activity.shared.edit {
            putString("current", new)
            commit()
        }
    private var answering: AnswerType
        get() = when (activity.shared.getString("answer_type", "none")) {
            "none" -> AnswerType.NONE
            "truth" -> AnswerType.TRUTH
            "action" -> AnswerType.ACTION
            else -> throw IllegalStateException()
        }
        set(status) = activity.shared.edit {
            putString(
                "answer_type",
                when (status) {
                    AnswerType.NONE -> "none"
                    AnswerType.TRUTH -> "truth"
                    AnswerType.ACTION -> "action"
                }
            )
            commit()
        }

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
            rollQuestion()
        }

        binding.actionRollAndClear.setOnClickListener {
            saveToHistory()
            rollQuestion()
        }

        binding.actionNext.setOnClickListener {
            saveToHistory()
            switchPlayer()
        }

        binding.taskType.text = when (answering) {
            AnswerType.NONE -> ""
            AnswerType.TRUTH -> getString(R.string.truth_text)
            AnswerType.ACTION -> getString(R.string.action)
        }
        fillNames(getPlayers())
        updateAnswering(answering)
        if (!current.isNullOrEmpty()) {
            binding.question.text = current
        }

        return binding.root
    }

    private fun rollQuestion() {
        when (answering) {
            AnswerType.NONE -> {}
            AnswerType.TRUTH -> generateTruth()
            AnswerType.ACTION -> generateAction()
        }
    }

    private fun switchPlayer() {
        val players = getPlayers()
        players.current = (players.current + 1) % (players.players.size)
        updatePlayers(players)

        updateAnswering(AnswerType.NONE)
    }

    private fun saveToHistory() {
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
    }

    private fun updateAnswering(status: AnswerType) {
        answering = status
        val players = getPlayers()
        when (answering) {
            AnswerType.ACTION, AnswerType.TRUTH -> {
                binding.ingameActions.visibility = View.VISIBLE
                binding.queueActions.visibility = View.INVISIBLE
            }

            AnswerType.NONE -> {
                binding.ingameActions.visibility = View.INVISIBLE
                binding.queueActions.visibility = View.VISIBLE

                binding.taskType.text = ""
                fillNames(players)
            }
        }
    }

    private fun fillNames(players: Players) {
        when (players.players.size) {
            0 -> {
                binding.question.text = getString(R.string.queue_of, "")
                binding.name.text = getString(R.string.name, "")
            }

            1 -> {
                binding.question.text =
                    getString(R.string.queue_of, players.players[0].name)
                binding.name.text =
                    getString(R.string.name, players.players[0].name)
            }

            else -> {
                binding.question.text =
                    getString(R.string.queue_of, players.players[players.current].name)
                binding.name.text =
                    getString(R.string.name, players.players[players.current].name)
            }
        }
        binding.round.text = getString(
            R.string.round,
            players.current + 1,
            players.players.size
        )
    }

    private fun generateTruth() {
        if (getPlayers().players.size <= 1) {
            binding.question.text = Html.fromHtml(
                getString(R.string.more_players), Html.FROM_HTML_MODE_COMPACT
            )
            return
        }

        updateAnswering(AnswerType.TRUTH)
        binding.taskType.text = "Правда"
        binding.question.text = ""
        binding.generationProgress.isVisible = true

        val aiGameInteractor = activity.aiGameInteractor

        binding.actionRoll.isEnabled = false
        binding.actionNext.isEnabled = false
        binding.actionRollAndClear.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            val players = getPlayers()
            processResult(
                aiGameInteractor.generateTruth(
                    players.players.map { it.name },
                    players.players[players.current].name,
                    activity.shared.getString("aiSettings", "")!!,
                    activity.historyCodec.decodeHistory(
                        activity.shared.getString(
                            "history", activity.historyCodec.encodeHistory(History())
                        )!!
                    )
                )
            )
        }
    }

    private suspend fun processResult(result: GenerateResult) {
        withContext(Dispatchers.Main) {
            binding.actionNext.isEnabled = false
            binding.actionRoll.isEnabled = true
            binding.question.text = when (result) {
                GenerateResult.IllegalRegion -> getString(R.string.illegal_region)
                GenerateResult.ParseError -> getString(R.string.parse_error)
                GenerateResult.QuotaLimit -> getString(R.string.quota_limit)
                GenerateResult.InsertToken -> getString(R.string.insert_token)
                is GenerateResult.HttpError -> getString(R.string.http_error, result.code)
                is GenerateResult.Success -> {
                    binding.actionRollAndClear.isEnabled = true
                    binding.actionNext.isEnabled = true
                    current = result.text
                    result.text
                }
            }
            binding.generationProgress.isVisible = false
        }
    }

    private fun generateAction() {
        if (getPlayers().players.size <= 1) {
            binding.question.text = Html.fromHtml(
                getString(R.string.more_players), Html.FROM_HTML_MODE_COMPACT
            )
            return
        }

        updateAnswering(AnswerType.ACTION)
        binding.taskType.text = "Действие"
        binding.question.text = ""
        binding.generationProgress.isVisible = true

        val aiGameInteractor = activity.aiGameInteractor

        binding.actionRoll.isEnabled = false
        binding.actionNext.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            val players = getPlayers()
            processResult(
                aiGameInteractor.generateAction(
                    players.players.map { it.name },
                    players.players[players.current].name,
                    activity.shared.getString("aiSettings", "")!!,
                    activity.historyCodec.decodeHistory(
                        activity.shared.getString(
                            "history", activity.historyCodec.encodeHistory(History())
                        )!!
                    )
                )
            )
        }
    }

    private fun getPlayers(): Players = activity.playersCodec.decodePlayers(
        activity.shared.getString(
            "players",
            activity.playersCodec.encodePlayers(Players())
        )!!
    )

    private fun updatePlayers(players: Players) = activity.shared.edit {
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