package ru.slavapmk.truthoraction.dto.game

data class History (
    val truths: MutableList<String> = mutableListOf(),
    val actions: MutableList<String> = mutableListOf(),
)
