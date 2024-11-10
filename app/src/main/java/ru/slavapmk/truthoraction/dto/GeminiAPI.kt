package ru.slavapmk.truthoraction.dto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.slavapmk.truthoraction.dto.gemini.request.GeminiRequest
import ru.slavapmk.truthoraction.dto.gemini.response.GeminiResponse

interface GeminiAPI {
    @POST("models/gemini-1.5-flash:generateContent")
    @Headers("Content-Type: application/json")
    suspend fun generate(@Query("key") token: String, @Body data: GeminiRequest): Response<GeminiResponse>
}