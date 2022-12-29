package Winter.pets.repository

import Winter.pets.domain.Country.Center
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CenterRepository :JpaRepository<Center,Long>{
    fun findByCenterNameAndGunguName(centerName:String,gunguName:String): Center;
    fun findBySiNameAndGunguName(siName:String,gunguName:String): List<Center>;
}