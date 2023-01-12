package Winter.pets.config

import io.swagger.models.HttpMethod
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override
    fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api")
            .allowedOrigins("http://localhost:8080")
            .allowedOrigins("http://localhost:5173")
            .allowCredentials(true)
            .allowedMethods("*")

            .maxAge(3000);
    }
    override fun configureAsyncSupport(confiqurer : AsyncSupportConfigurer):Unit {
        confiqurer.setDefaultTimeout(100000)
        confiqurer.registerCallableInterceptors(timeoutInterceptor())
    }
    fun timeoutInterceptor():TimeoutCallableProcessingInterceptor{
        return TimeoutCallableProcessingInterceptor()
    }
    @Bean
    fun corsConfigurationSource() : CorsConfigurationSource{
        val cors = CorsConfiguration()
        cors.addAllowedOrigin("http://localhost:5173")
        cors.addAllowedOrigin("http://localhost:8080")
        cors.addAllowedMethod("*")
        cors.addAllowedHeader("*")
        cors.setMaxAge(7200L)
        val url =UrlBasedCorsConfigurationSource()
        url.registerCorsConfiguration("/**",cors)
        return url

    }
}