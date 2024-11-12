package ru.slavapmk.truthoraction.dto.game

import java.util.UUID

data class Player(
    val name: String,
    val id: UUID = UUID.randomUUID()
)
