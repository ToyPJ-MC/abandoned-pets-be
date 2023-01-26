package Winter.pets


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication
@ConfigurationPropertiesScan
class PetsApplication

fun main(args: Array<String>) {
	runApplication<PetsApplication>(*args)
}
