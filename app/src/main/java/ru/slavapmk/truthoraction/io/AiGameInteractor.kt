package ru.slavapmk.truthoraction.io

import ru.slavapmk.truthoraction.dto.History

class AiGameInteractor(
    private val executor: TextGenerator
) : GameInteractor {
    override suspend fun generateTruth(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult {
        return executor.generateText(
            "Нужен *вопрос* для игрока $player", players, additional, history
        )
    }

    override suspend fun generateAction(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult {
        return executor.generateText(
            "Нужно *действие* для игрока $player", players, additional, history
        )
    }
}