package Winter.pets.service

interface KindService {
    fun findToKind(kindName: String): List<String>
    fun addToKind(kindName: String)
}