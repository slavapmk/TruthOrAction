package ru.slavapmk.truthoraction.dto.gemini.response

data class GeminiResponse(
    val candidates: List<GeminiResponseCandidate>
)


data class GeminiResponseCandidate(
    val content: GeminiResponseContent
)

class GeminiResponseContent(
    val parts: List<GeminiResponsePart>
)

data class GeminiResponsePart(
    val text: String
)