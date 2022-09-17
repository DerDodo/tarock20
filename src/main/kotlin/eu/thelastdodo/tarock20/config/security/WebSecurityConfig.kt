package eu.thelastdodo.tarock20.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
	var corsConfig: CorsConfig
): WebSecurityConfigurerAdapter() {
	override fun configure(http: HttpSecurity) {
		http
			.csrf().disable().cors().and()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/**").permitAll()
			.anyRequest().denyAll()
	}

	// provide static resources
	override fun configure(web: WebSecurity) {
		web.ignoring()
			.antMatchers("/**/*.{js|css|html}")
			.antMatchers("/favicon.ico")
	}

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource? {
		val configuration = CorsConfiguration()
		configuration.allowedOrigins = corsConfig.allowedOrigins.asList()
		configuration.allowedMethods = listOf("GET", "POST")
		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", configuration)
		return source
	}
}
