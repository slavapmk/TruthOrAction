package ru.slavapmk.truthoraction.io

interface TextGenerator {
    suspend fun generateText(prompt: String, players: List<String>, additional: String): String?
}