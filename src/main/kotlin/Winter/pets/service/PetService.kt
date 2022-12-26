package Winter.pets.service

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si

interface PetService {

    fun addToCountry():Unit;

    fun addToGu(name:String):Unit;

    fun findToGu(name:String):List<String>;

    fun addToCenter(siName:String,gunguName:String):Unit
    fun findToCenter(siName:String,gunguName:String):List<String>;
}