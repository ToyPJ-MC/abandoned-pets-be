package Winter.pets.service

interface GunguService {
    fun findToGu(name: String): List<String>
    fun addToGu(name: String)
}