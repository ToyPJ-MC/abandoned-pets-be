package Winter.pets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetsApplication

fun main(args: Array<String>) {
	runApplication<PetsApplication>(*args)
}
