package Winter.pets.repository

import Winter.pets.domain.kind.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AddToPetRepository  : JpaRepository<Pet,Long>{
    fun findByDesertionNo (desertion : String): String?{
        return "null"
    }
    fun findByOrgNmAndNeuterYnAndAndKindCd (orgNm : String,neuterYn : String,kindCd:String): List<Pet>?{
        return null
    }
}