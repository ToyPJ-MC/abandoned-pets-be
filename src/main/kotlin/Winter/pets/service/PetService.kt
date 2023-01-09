package Winter.pets.service
import Winter.pets.domain.kind.AddPets
import Winter.pets.domain.kind.SelectPets
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PetService {

    fun selectToPet(Start:String,end:String,kindCode:String,kind:String,
                  si:String,gungu:String,centerCode:String,state:String,neuter:String): List<SelectPets>
    fun findToPet(page:Int,size:Int): List<AddPets>
    fun findToMaxPage():String
    fun addToPet():Unit
    fun findToSearchList():List<SelectPets>

    fun deleteToPet():Unit

}