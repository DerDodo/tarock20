package eu.thelastdodo.tarock20.repository

import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.enums.GamePhase
import org.springframework.stereotype.Repository

@Repository
class GameRepository {
    private val games: MutableList<Game> = mutableListOf()

    fun findGame(gameId: String): Game? {
        return games.find { it.id == gameId }
    }

    fun findAllOpenGames(playerId: String): List<Game> {
        return games.filter { game -> isOpen(game) || game.isInGame(playerId) }
    }

    private fun isOpen(game: Game): Boolean {
        return game.phase == GamePhase.NotYetStarted && game.players.size < 4
    }

    fun addGame(game: Game) {
        games.add(game)
    }
}