package eu.thelastdodo.tarock20.config.security

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class WebSocketAuthenticatorService {
	fun getAuthenticatedOrFail(username: String?, password: String?): UsernamePasswordAuthenticationToken {
		if (username == null || username.trim().isEmpty()) {
			throw AuthenticationCredentialsNotFoundException("Username was null or empty.")
		}

		if (password == null || password.trim().isEmpty()) {
			throw AuthenticationCredentialsNotFoundException("Password was null or empty.")
		}

		return UsernamePasswordAuthenticationToken(
				username,
				null,
				listOf(SimpleGrantedAuthority("USER"))
		)
	}
}
