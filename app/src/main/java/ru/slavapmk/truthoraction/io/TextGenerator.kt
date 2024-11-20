package ru.slavapmk.truthoraction.io

interface TextGenerator {
    suspend fun generateText(
        prompt: AiPrompts
    ): GenerateResult
}

sealed interface GenerateResult {
    data object QuotaLimit : GenerateResult
    data object ParseError : GenerateResult
    data object IllegalRegion : GenerateResult
    data object InsertToken : GenerateResult
    data class Success(val text: String) : GenerateResult
    data class HttpError(val code: Int) : GenerateResult
}