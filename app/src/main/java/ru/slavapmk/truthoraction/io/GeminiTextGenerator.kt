package ru.slavapmk.truthoraction.io

import android.util.Log
import com.google.gson.Gson
import ru.slavapmk.truthoraction.dto.GeminiAPI
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
    override suspend fun generateText(prompt: String, players: List<String>): String? {
        val users = model.getUsers(
            token,
            GeminiRequest(
                listOf(
                    GeminiRequestContent(
                        listOf(
                            GeminiRequestPart("Выступи в роли генератора вопросов и действий для одноимённой игры правда или действие. Твоя основная задача придумать смешную задачу для компании (можно и задать философский вопрос). Главное не повторяйся. У тебя есть список уже предложенных тобой действий или вопросов. Не допускай повторения. Максимум что ты можешь повторить, это задачу из действия как-то использовать в вопросе, и наоборот. А при одинаковом типе заданий используй как можно меньше повторений. При желании можешь использовать других людей для задания человеку. Отвечай на русском."),
                            GeminiRequestPart("Список использованных вопросов:"),
                            GeminiRequestPart("Дополнительные настройки:"),
                            GeminiRequestPart("В игре присутствуют 3 человека: " + players.joinToString(", ")),
                        )
                    ),
                    GeminiRequestContent(
                        listOf(GeminiRequestPart("Хорошо, я выдам правду или действие. Ничего более")),
                        role = "model"
                    ),
                    GeminiRequestContent(
                        listOf(GeminiRequestPart(prompt)),
                    )
                ),
                GeminiGenerationConfig()
            ),
        )
        Log.d("ferwdq", users.body().toString())
        val text = users.body()?.candidates?.get(0)?.content?.parts?.get(0)?.text ?: return null
        val fromJson = gson.fromJson(text, GameQuestion::class.java)
        return fromJson.task
    }
}