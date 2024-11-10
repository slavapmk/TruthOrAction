package ru.slavapmk.truthoraction.dto

import com.google.gson.Gson
import ru.slavapmk.truthoraction.dto.game.Players

class PlayersCodec(private val gson: Gson) {
    fun encodePlayers(history: Players) = gson.toJson(history)
    fun decodePlayers(encoded: String) = gson.fromJson(
        encoded,
        Players::class.java
    )
}