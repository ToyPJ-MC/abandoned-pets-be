package Winter.pets.config

import io.swagger.models.HttpMethod
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
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
        registry.addMapping("/**")
            .allowedOriginPatterns("http://192.168.0.14:5173")
/*
            .allowedOrigins("http://localhost:8080")
            .allowedOrigins("http://192.168.0.16:8080/")
            .allowedOrigins("http://192.168.0.14:5173")
            .allowedOrigins("http://192.168.0.16:8080/oauth2/authorization/kakao")
*/
            .allowedOrigins("*")
            .allowCredentials(false)
            .allowedHeaders("*")
            .allowedMethods("GET","POST")
            .maxAge(3600);
    }
    override fun configureAsyncSupport(confiqurer : AsyncSupportConfigurer):Unit {
        confiqurer.setDefaultTimeout(100000)
        confiqurer.registerCallableInterceptors(timeoutInterceptor())
    }
    fun timeoutInterceptor():TimeoutCallableProcessingInterceptor{
        return TimeoutCallableProcessingInterceptor()
    }

}