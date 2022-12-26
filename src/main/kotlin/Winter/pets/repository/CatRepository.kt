package Winter.pets.repository

import Winter.pets.domain.kind.Cat
import org.springframework.data.jpa.repository.JpaRepository

interface CatRepository:JpaRepository<Cat,Long> {

}