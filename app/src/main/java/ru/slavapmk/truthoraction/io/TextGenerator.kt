package ru.slavapmk.truthoraction.io

import ru.slavapmk.truthoraction.dto.History

interface TextGenerator {
    suspend fun generateText(
        prompt: String, players: List<String>, additional: String, history: History
    ): String?
}