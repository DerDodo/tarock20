package eu.thelastdodo.tarock20.entity

import eu.thelastdodo.tarock20.entity.enums.GamePhase
import eu.thelastdodo.tarock20.entity.enums.TeamType
import java.util.*

class Game(
    firstPlayerId: String,
) {
    val players: MutableList<Player>
    val id: String = UUID.randomUUID().toString()
    var phase: GamePhase = GamePhase.NotYetStarted
    var foreHand: Player
    var foreHandPartner: Player? = null
    var otherTeam: List<Player> = listOf()
    var activePlayer: Player
    var firstPlayerInTurn: Player

    val foreHandTeam: List<Player> get() {
        return if (foreHandPartner == null)
            listOf(foreHand)
        else
            listOf(foreHand, foreHandPartner!!)
    }

    init {
        foreHand = Player(firstPlayerId)
        players = mutableListOf(foreHand)
        activePlayer = foreHand
        firstPlayerInTurn = foreHand
    }

    fun isInGame(playerId: String): Boolean {
        return players.any { it.id == playerId }
    }

    fun isInGame(player: Player): Boolean {
        return players.contains(player)
    }

    fun getTeamOf(player: Player): TeamType {
        return if (foreHandTeam.contains(player)) TeamType.ForeHand else TeamType.Other
    }

    companion object {
        const val MAX_PLAYER_SIZE = 4
    }
}