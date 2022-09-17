package eu.thelastdodo.tarock20.config.security

import eu.thelastdodo.tarock20.entity.UserData
import eu.thelastdodo.tarock20.repository.UserDataRepository
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class AuthChannelInterceptorAdapter(
	private val webSocketAuthenticatorService: WebSocketAuthenticatorService,
	private val userDataRepository: UserDataRepository
): ChannelInterceptor {

	override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
		val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

		if (accessor != null && StompCommand.CONNECT == accessor.command) {
			val playerId = accessor.getFirstNativeHeader(PLAYER_ID_HEADER)!!
			val password = accessor.getFirstNativeHeader(PASSWORD_HEADER)!!

			val user = webSocketAuthenticatorService.getAuthenticatedOrFail(playerId, password)

			userDataRepository.put(playerId, UserData(playerId))
			accessor.user = user
		}
		return message
	}

	companion object {
		private const val PLAYER_ID_HEADER = "login"
		private const val PASSWORD_HEADER = "passcode"
	}
}
