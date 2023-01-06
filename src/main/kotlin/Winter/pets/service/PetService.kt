package Winter.pets.service
import Winter.pets.domain.kind.SelectPets
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PetService {

    fun addToCountry():Unit;

    fun addToGu(name:String):Unit;

    fun findToGu(name:String):List<String>;

    fun addToCenter(siName:String,gunguName:String):Unit
    fun findToCenter(siName:String,gunguName:String):List<String>
    fun addToKind(kindName:String):Unit
    fun findToKind(kindName:String):List<String>
    fun addToPet(Start:String,end:String,kindCode:String,kind:String,
                  si:String,gungu:String,centerCode:String,state:String,neuter:String): List<SelectPets>
    fun findToPet(page:Int,size:Int): List<SelectPets>
    fun findToMaxPage():String
}