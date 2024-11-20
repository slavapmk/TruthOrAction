package ru.slavapmk.truthoraction.io

import android.content.Context
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.dto.game.History

class AiGameInteractor(
    private val executor: TextGenerator,
    private val context: Context
) : GameInteractor {
    override suspend fun generateTruth(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult {
        return executor.generateText(
            AiPrompts(
                context.getString(R.string.main_prompt),
                context.getString(
                    R.string.used,
                    history.truths.joinToString(";\n"),
                    history.actions.joinToString(";\n")
                ),
                context.getString(R.string.additional, additional),
                context.getString(
                    R.string.players,
                    players.size, players.joinToString(", ")
                ),
                context.getString(R.string.confirm),
                context.getString(R.string.truth_request)
            )
        )
    }

    override suspend fun generateAction(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult {
        return executor.generateText(
            AiPrompts(
                context.getString(R.string.main_prompt),
                context.getString(
                    R.string.used,
                    history.truths.joinToString(";\n"),
                    history.actions.joinToString(";\n")
                ),
                context.getString(R.string.additional, additional),
                context.getString(
                    R.string.players,
                    players.size, players.joinToString(", ")
                ),
                context.getString(R.string.confirm),
                context.getString(R.string.action_request)
            )
        )
    }
}