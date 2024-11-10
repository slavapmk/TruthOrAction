package ru.slavapmk.truthoraction.io

import ru.slavapmk.truthoraction.dto.game.History

interface TextGenerator {
    suspend fun generateText(
        prompt: String, players: List<String>, additional: String, history: History
    ): GenerateResult
}

sealed interface GenerateResult {
    data object QuotaLimit : GenerateResult
    data object ParseError : GenerateResult
    data object IllegalRegion : GenerateResult
    data class Success(val text: String) : GenerateResult
    data class HttpError(val code: Int) : GenerateResult
}