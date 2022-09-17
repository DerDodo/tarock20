package eu.thelastdodo.tarock20.controller.ws

import eu.thelastdodo.tarock20.config.RequestMappingPath
import eu.thelastdodo.tarock20.controller.ErrorDetails
import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.entity.Game
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class GameBroker(
	private val messagingTemplate: SimpMessagingTemplate,
) {
	fun sendGameMessage(game: Game, playerId: String) {
		messagingTemplate.convertAndSendToUser(
			playerId,
			RequestMappingPath.wsOutGameUpdate(game.id),
			GameMapper.gameToGameDto(game, playerId))
	}

	fun sendGameError(gameId: String, principal: Principal, exception: GameException) {
		val response = ErrorDetails(exception::class.simpleName!!)
		messagingTemplate.convertAndSendToUser(
			principal.name,
			RequestMappingPath.wsOutError(gameId),
			response)
	}
}
