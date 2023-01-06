package Winter.pets.repository

import Winter.pets.domain.kind.SelectPets
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PetsRepository : JpaRepository<SelectPets,Long> {

}