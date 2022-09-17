package eu.thelastdodo.tarock20.config

import eu.thelastdodo.tarock20.config.security.AuthChannelInterceptorAdapter
import eu.thelastdodo.tarock20.config.security.CorsConfig
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@EnableWebSocketMessageBroker
@Configuration
class WebSocketConfig(
	private val authChannelInterceptorAdapter: AuthChannelInterceptorAdapter,
	private val corsConfig: CorsConfig
): WebSocketMessageBrokerConfigurer {
	override fun configureMessageBroker(config: MessageBrokerRegistry) {
		config.enableSimpleBroker(RequestMappingPath.WS_PUBLIC_BASE_PATH)
		config.setApplicationDestinationPrefixes(RequestMappingPath.WS_INCOMING_BASE_PATH)
		config.setUserDestinationPrefix(RequestMappingPath.WS_PRIVATE_BASE_PATH)
	}

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint(RequestMappingPath.WS_REGISTRY_PATH)
			.setAllowedOrigins(*corsConfig.allowedOrigins)
			.withSockJS()
	}

	override fun configureClientInboundChannel(registration: ChannelRegistration) {
		registration.interceptors(authChannelInterceptorAdapter)
	}
}
