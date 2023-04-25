package Winter.pets.repository

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface gunguRepository:JpaRepository<GunGu,Long> {
    fun findBysiName(name:String): List<GunGu>;
    fun findBygunguName(name:String):GunGu;
    //@Query("SELECT m from GunGu m where m.siName = siname and m.gunguName = gunguname")
    fun findBySiNameAndGunguName(siName:String,gunguName:String) : GunGu

}