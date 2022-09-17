package eu.thelastdodo.tarock20.domain

import eu.thelastdodo.tarock20.entity.Game

data class OpenGameDto(
    val id: String,
    val players: List<String>,
) {
    constructor(game: Game): this(game.id, game.players.map { it.id })
}