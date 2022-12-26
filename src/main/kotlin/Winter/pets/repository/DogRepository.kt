package Winter.pets.repository

import Winter.pets.domain.kind.Dog
import org.springframework.data.jpa.repository.JpaRepository

interface DogRepository:JpaRepository<Dog,Long> {

}