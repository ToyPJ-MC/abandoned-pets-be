package Winter.pets.domain.jwt.repository

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.jwt.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Likes,Long>{

    fun findByMemberId(memberId: String):Likes?{
        return null
    }
}