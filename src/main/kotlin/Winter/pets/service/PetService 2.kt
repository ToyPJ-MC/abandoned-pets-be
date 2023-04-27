package Winter.pets.service
import Winter.pets.domain.kind.Pet


interface PetService {

    fun findToPet(page:Int,size:Int): List<Pet>
    fun findToMaxPage():String
    fun deleteToSearchList(memberid: String)
    fun allToPet():String
    fun selectToPet(memberid: String, kindCd:String,careNm:String,orgNm : String,neuterYn : String): Any
}