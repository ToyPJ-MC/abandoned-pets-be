package Winter.pets.domain.jwt.repository

import Winter.pets.domain.jwt.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member,Long> {

    fun findByEmail (email:String):Member?{
        return null
    }
}