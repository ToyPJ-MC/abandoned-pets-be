package Winter.pets.service

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si

interface PetService {

    fun findCountry():List<Si>;
    fun findGu(name:String):List<String>;
}