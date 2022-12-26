package Winter.pets.service

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import org.json.JSONArray
import org.json.JSONObject

interface PetService {

    fun addToCountry():Unit;

    fun addToGu(name:String):Unit;

    fun findToGu(name:String):List<String>;

    fun addToCenter(siName:String,gunguName:String):Unit
    fun findToCenter(siName:String,gunguName:String):List<String>
    fun addToKind(kindName:String):Unit
    fun findToKind(kindName:String):List<String>
    fun findToPet(Start:String,end:String,kindCode:String,kind:String,
                  si:String,gungu:String,centerCode:String,state:String,neuter:String):List<String>
}