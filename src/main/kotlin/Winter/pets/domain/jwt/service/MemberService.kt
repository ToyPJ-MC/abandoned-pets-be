package Winter.pets.domain.jwt.service

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.jwt.provider.JwtProvider
import Winter.pets.domain.jwt.repository.LikeRepository
import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.Pet
import Winter.pets.repository.AddToPetRepository
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Service @EnableScheduling
class MemberService (
    private val memberRepo:MemberRepository,
    private val likeRepo: LikeRepository,
    private val petRepo : AddToPetRepository,
    private val jwtProvider: JwtProvider
){
    //고안 중
   fun deleteToSelectPet(memberId:String, noticeNo:List<String>) {
        var member = memberRepo.findById(memberId)
            for(i in 0 until noticeNo.size){
                for(j in 0 until member!!.list.size){
                    if(member.list.get(j).noticeNo.equals(noticeNo.get(i))){
                        member.list.remove(member.list.get(j))
                    }
                }
            }
        }

    /*************유저 ID로 최근 검색 조회**************/ //complete
    fun findToList(token:String): List<Pet> {
        var getEmail = jwtProvider.getEmail(token)
        var member = memberRepo.findByEmail(getEmail)
        var list :ArrayList<Pet> = ArrayList()
        for(i in 0 until member?.list!!.size){
            list.add(member.list.get(i))
        }
        var without = list.distinctBy { it.noticeNo }
        return without
    }
    /************ member like 기능 **************/
    fun addToLikes(token: String,noticeNo:String){
        var getEmail = jwtProvider.getEmail(token)
        var member = memberRepo.findByEmail(getEmail)
        var pet = petRepo.findByNoticeNo(noticeNo) //공고 번호로 해당 pet 찾기
        var likes:Likes? = likeRepo.findByMemberEmail(member!!.email!!)//memberId로 like 조회
        if(likes == null){ //like가 없을때
            var like = Likes()
            like.member = memberRepo.findByEmail(getEmail)!!
            like.list.add(pet!!)
            likeRepo.save(like)
        }
        else{ //like가 있을때
           likes.list.add(pet!!)
            likeRepo.save(likes)
        }
    }
    /************ like 목록 조회 **************/
    fun findToLikesList(memberId: String):List<Pet> { //like list 조회 == pet
        var like:Likes? = likeRepo.findByMemberId(memberId)
        var list :ArrayList<Pet> = ArrayList()
        for(i in 0 until like?.list!!.size){
            list.add(like.list.get(i))
        }
        var without = list.distinctBy { it.noticeNo }
        return without
    }
}