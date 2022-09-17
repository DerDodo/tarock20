package eu.thelastdodo.tarock20.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
@ConfigurationProperties(prefix = "cors")
class MvcConfiguration(
	var corsConfig: CorsConfig
) : WebMvcConfigurer {

	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/")
			.resourceChain(true)
			.addResolver(object : PathResourceResolver() {
				override fun getResource(resourcePath: String, location: Resource): Resource {
					val requestedResource = location.createRelative(resourcePath)

					return if (requestedResource.exists() && requestedResource.isReadable) requestedResource else ClassPathResource(
						"/static/index.html"
					)
				}
			})
	}

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**").allowedMethods("GET", "POST").allowedOrigins(*corsConfig.allowedOrigins)
	}
}
