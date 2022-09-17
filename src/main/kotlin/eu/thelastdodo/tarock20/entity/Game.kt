package eu.thelastdodo.tarock20.entity

import java.util.*

class Game(
    firstPlayerId: String,
    val id: String = UUID.randomUUID().toString(),
    val phase: GamePhase = GamePhase.NotYetStarted,
) {
    val players: MutableList<Player>

    init {
        players = mutableListOf(Player(firstPlayerId))
    }

    fun isInGame(playerId: String): Boolean {
        return players.any { it.id == playerId }
    }

    companion object {
        const val MAX_PLAYER_SIZE = 4
    }
}