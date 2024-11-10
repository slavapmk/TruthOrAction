package ru.slavapmk.truthoraction.io

import ru.slavapmk.truthoraction.dto.History

interface GameInteractor {
    suspend fun generateTruth(
        players: List<String>, player: String, additional: String, history: History
    ): String
    suspend fun generateAction(
        players: List<String>, player: String, additional: String, history: History
    ): String
}