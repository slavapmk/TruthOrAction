package ru.slavapmk.truthoraction.io

data class AiPrompts(
    val mainPrompt: String,
    val used: String,
    val additional: String,
    val players: String,
    val confirm: String,
    val prompt: String
)
