package ru.slavapmk.truthoraction.dto.gemini.request

data class GeminiRequestContent(
    val parts: List<GeminiRequestPart>,
    val role: String = "user"
)
