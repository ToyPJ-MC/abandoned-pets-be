package Winter.pets.repository

import Winter.pets.domain.Country.Si
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CountryRepository:JpaRepository<Si,Long> {
    fun findByName(name:String):Si;
    fun findAllBy(): List<Si>;
}
