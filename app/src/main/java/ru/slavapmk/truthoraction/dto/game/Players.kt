package ru.slavapmk.truthoraction.dto.game

data class Players (
    var current: Int = 0,
    val players: MutableList<Player> = mutableListOf(
        Player("Вася"),
        Player("Петя"),
    )
)
