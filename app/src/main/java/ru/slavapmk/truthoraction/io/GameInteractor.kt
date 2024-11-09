package ru.slavapmk.truthoraction.io

interface GameInteractor {
    suspend fun generateTruth(players: List<String>, player: String): String
    suspend fun generateAction(players: List<String>, player: String): String
}