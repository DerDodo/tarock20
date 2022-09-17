package eu.thelastdodo.tarock20.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "cors")
data class CorsConfig(
	var allowedOrigins: Array<String>
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as CorsConfig

		if (!allowedOrigins.contentEquals(other.allowedOrigins)) return false

		return true
	}

	override fun hashCode(): Int {
		return allowedOrigins.contentHashCode()
	}
}