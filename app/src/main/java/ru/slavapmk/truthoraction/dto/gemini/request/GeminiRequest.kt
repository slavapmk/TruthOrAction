package ru.slavapmk.truthoraction.dto.gemini.request

data class GeminiRequest(
    val contents: List<GeminiRequestContent>,
    val generationConfig: GeminiGenerationConfig,
    val safetySettings: List<SafetySetting> = listOf(
        SafetySetting("HARM_CATEGORY_HARASSMENT"),
        SafetySetting("HARM_CATEGORY_HATE_SPEECH"),
        SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT"),
        SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT"),
        SafetySetting("HARM_CATEGORY_CIVIC_INTEGRITY"),
    )
)

data class SafetySetting(
    val category: String,
    val threshold: String = "BLOCK_NONE"
)

data class GeminiGenerationConfig(
    val response_mime_type: String = "application/json",
    val response_schema: GeminiGenerationSchema? = GeminiGenerationSchema()
)

data class GeminiGenerationSchema(
    val type: String = "OBJECT",
    val properties: GeminiGenerationProps = GeminiGenerationProps()
)

class GeminiGenerationProps(
    @SuppressWarnings("unused")
    val task: GeminiSchemaString = GeminiSchemaString(),
)

class GeminiSchemaString(
    @SuppressWarnings("unused")
    val type: String = "STRING"
)