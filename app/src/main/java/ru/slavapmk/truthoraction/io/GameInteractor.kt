package ru.slavapmk.truthoraction.io

import ru.slavapmk.truthoraction.dto.game.History

interface GameInteractor {
    suspend fun generateTruth(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult
    suspend fun generateAction(
        players: List<String>, player: String, additional: String, history: History
    ): GenerateResult
}