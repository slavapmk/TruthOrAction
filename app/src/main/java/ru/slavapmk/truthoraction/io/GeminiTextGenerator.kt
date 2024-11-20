package ru.slavapmk.truthoraction.io

import android.util.Log
import com.google.gson.Gson
import ru.slavapmk.truthoraction.dto.GeminiAPI
import ru.slavapmk.truthoraction.dto.game.History
import ru.slavapmk.truthoraction.dto.game.GameQuestion
import ru.slavapmk.truthoraction.dto.gemini.request.GeminiGenerationConfig
import ru.slavapmk.truthoraction.dto.gemini.request.GeminiRequest
import ru.slavapmk.truthoraction.dto.gemini.request.GeminiRequestContent
import ru.slavapmk.truthoraction.dto.gemini.request.GeminiRequestPart

class GeminiTextGenerator(
    private val token: String,
    private val model: GeminiAPI,
    private val gson: Gson
) : TextGenerator {
    override suspend fun generateText(
        prompt: AiPrompts
    ): GenerateResult {
        val content = GeminiRequestContent(
            listOf(
                GeminiRequestPart(prompt.mainPrompt),
                GeminiRequestPart(prompt.used),
                GeminiRequestPart(prompt.additional),
                GeminiRequestPart(prompt.players),
            )
        )
        val generate = model.generate(
            token,
            GeminiRequest(
                listOf(
                    content,
                    GeminiRequestContent(
                        listOf(GeminiRequestPart(prompt.confirm)),
                        role = "model"
                    ),
                    GeminiRequestContent(
                        listOf(GeminiRequestPart(prompt.prompt)),
                    )
                ),
                GeminiGenerationConfig()
            ),
        )
        when (generate.code()) {
            200 -> {
                val text = generate.body()?.candidates?.get(0)?.content?.parts?.get(0)?.text
                    ?: return GenerateResult.ParseError
                return GenerateResult.Success(
                    gson.fromJson(text, GameQuestion::class.java).task
                )
            }

            400 -> return GenerateResult.IllegalRegion
            403 -> return GenerateResult.InsertToken
            429 -> return GenerateResult.QuotaLimit
            else -> return GenerateResult.HttpError(generate.code())
        }
    }
}