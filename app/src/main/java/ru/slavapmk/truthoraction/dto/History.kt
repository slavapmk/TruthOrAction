package ru.slavapmk.truthoraction.dto

data class History (
    val truths: MutableList<String> = mutableListOf(),
    val actions: MutableList<String> = mutableListOf(),
)
