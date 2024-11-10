package ru.slavapmk.truthoraction.dto

import com.google.gson.Gson
import ru.slavapmk.truthoraction.dto.game.History

class HistoryCodec(private val gson: Gson) {
    fun encodeHistory(history: History) = gson.toJson(history)
    fun decodeHistory(encoded: String) = gson.fromJson(
        encoded,
        History::class.java
    )
}