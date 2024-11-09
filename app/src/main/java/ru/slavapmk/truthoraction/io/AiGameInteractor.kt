package ru.slavapmk.truthoraction.io

class AiGameInteractor(
    private val executor: TextGenerator
) : GameInteractor {
    override suspend fun generateTruth(players: List<String>, player: String): String {
        return executor.generateText(
            "Нужен *вопрос* для игрока $player", players
        ) ?: "Ошибка генерации"
    }

    override suspend fun generateAction(players: List<String>, player: String): String {
        return executor.generateText(
            "Нужно *действие* для игрока $player", players
        ) ?: "Ошибка генерации"
    }
}