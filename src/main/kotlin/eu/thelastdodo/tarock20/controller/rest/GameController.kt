package eu.thelastdodo.tarock20.controller

import eu.thelastdodo.tarock20.config.RequestMappingPath
import eu.thelastdodo.tarock20.domain.OpenGameDto
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.GamePhase
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.repository.GameRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

class PlayerAlreadyInGameException: GameException()
class GameFullException: GameException()
class GameAlreadyStartedException: GameException()
class GameNotFoundException: GameException()

@RestController
class GameController(
    private val gameRepository: GameRepository,
) {
    @GetMapping(RequestMappingPath.GAMES_BASE_PATH)
    fun games(userId: String): Iterable<OpenGameDto> {
        return gameRepository.findAllOpenGames(userId).map { OpenGameDto(it) }
    }

    @PostMapping(RequestMappingPath.GAMES_BASE_PATH)
    fun createNewGame(userId: String): String {
        val game = Game(userId)
        gameRepository.addGame(game)
        return game.id
    }

    @PostMapping(RequestMappingPath.JOIN_GAME)
    fun join(gameId: String, userId: String) {
        val game = gameRepository.findGame(gameId) ?: throw GameNotFoundException()

        if (game.phase != GamePhase.NotYetStarted) {
            throw GameAlreadyStartedException()
        }

        if (game.players.any { it.id == userId }) {
            throw PlayerAlreadyInGameException()
        }

        if (game.players.size > Game.MAX_PLAYER_SIZE) {
            throw GameFullException()
        }

        game.players.add(Player(userId))
    }
}