package eu.thelastdodo.tarock20.controller.ws

import eu.thelastdodo.tarock20.config.RequestMappingPath
import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.controller.GameNotFoundException
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.repository.GameRepository
import eu.thelastdodo.tarock20.repository.UserDataRepository
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.security.Principal

class PlayerAlreadyInGameException: GameException()
class PlayerNotInGameException: GameException()

@Controller
class PlayerActionBroker(
	private val gameRepository: GameRepository,
	private val gameBroker: GameBroker,
	private val userDataRepository: UserDataRepository,
) {


	@MessageMapping(RequestMappingPath.WS_IN_SYNC)
	fun sync(
		@DestinationVariable gameId: String,
		principal: Principal,
	) {
		handle(gameId, principal) { }
	}

	private fun handle(gameId: String, principal: Principal, callback: (game: Game) -> Unit) {
		try {
			val game = gameRepository.findGame(gameId) ?: throw GameNotFoundException()
			callback(game)
			gameBroker.sendGameMessage(game, principal.name)
		} catch(exception: GameException) {
			gameBroker.sendGameError(gameId, principal, exception)
		}
	}

	private fun getPlayer(game: Game, principal: Principal): Player {
		return getPlayer(game, principal.name)
	}

	private fun getPlayer(game: Game, playerId: String): Player {
		val userData = userDataRepository.get(playerId) ?: throw PlayerNotInGameException()
		return game.players.firstOrNull { userData.playerId == it.id } ?: throw PlayerNotInGameException()
	}
}
