package Winter.pets.repository

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface gunguRepository:JpaRepository<GunGu,Long> {
    fun findBysiName(name:String): List<GunGu>;
}