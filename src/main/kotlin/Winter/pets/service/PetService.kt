package Winter.pets.service

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si

interface PetService {

    fun addToCountry():Unit;

    fun addToGu(name:String):Unit;

    fun findToGu(name:String):List<String>;
}