package eu.thelastdodo.tarock20.controller.ws

import eu.thelastdodo.tarock20.config.RequestMappingPath
import eu.thelastdodo.tarock20.controller.ErrorDetails
import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.service.CardsWereHandedOut
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class GameBroker(
	private val messagingTemplate: SimpMessagingTemplate,
) {
	fun gameSync(game: Game, playerId: String) {
		messagingTemplate.convertAndSendToUser(
			playerId,
			RequestMappingPath.wsOutGameSync(game.id),
			GameMapper.gameToGameDto(game, playerId))
	}

	fun gameStarted(game: Game) {
		val payload: Any = ""
		messagingTemplate.convertAndSend(
			RequestMappingPath.wsOutGameStarted(game.id),
			payload)
	}

	@EventListener
	fun cardsWereHandedOut(event: CardsWereHandedOut) {
		event.game.players.forEach { player ->
			messagingTemplate.convertAndSendToUser(
				player.id,
				RequestMappingPath.wsOutGameHandoutCards(event.game.id),
				player.hand)
		}
	}

	fun playerJoined(game: Game, player: Player) {
		messagingTemplate.convertAndSend(
			RequestMappingPath.wsOutGamePlayerJoined(game.id),
			GameMapper.playerToOtherPlayerDto(player)
		)
	}

	fun sendGameError(gameId: String, principal: Principal, exception: GameException) {
		val response = ErrorDetails(exception::class.simpleName!!)
		messagingTemplate.convertAndSendToUser(
			principal.name,
			RequestMappingPath.wsOutError(gameId),
			response)
	}
}
