package Winter.pets.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("Winter.pets.controller"))
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .apiInfo(this.apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Abandonded Pets Find API")
            .description("Choi && Moon Project")
            .version("1.0")
            .contact(
                Contact(
                    "Good",
                    "https://www.janeshop.kr",
                    "moon125774@gmail.com"
                )
            )
            .build()
    }
}