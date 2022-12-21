package Winter.pets.repository

import Winter.pets.domain.Country.GunGu
import org.springframework.data.jpa.repository.JpaRepository

interface gunguRepository:JpaRepository<GunGu,Long> {
}