package Winter.pets.service

interface CenterService {
    fun findToCenter(siName: String, gunguName: String): List<String>
    fun addToCenter(siName: String, gunguName: String)
}