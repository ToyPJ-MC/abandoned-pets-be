package Winter.pets.config

import io.swagger.models.HttpMethod
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override
    fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api")
            .allowedOrigins("http://203.241.228.50:18000")
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
}