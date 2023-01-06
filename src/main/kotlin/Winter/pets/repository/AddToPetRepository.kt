package Winter.pets.repository

import Winter.pets.domain.kind.AddPets
import org.springframework.data.jpa.repository.JpaRepository

interface AddToPetRepository  : JpaRepository<AddPets,Long>{
    fun findByDesertionNo (desertion : String): String?{
        return "null"
    }
}