package Winter.pets.domain.jwt.service

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.jwt.repository.LikeRepository
import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.Pet
import Winter.pets.repository.AddToPetRepository
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@Service @EnableScheduling
class MemberService (
    private val memberRepo:MemberRepository,
    private val likeRepo: LikeRepository,
    private val petRepo : AddToPetRepository
){
    //고안 중
   fun deleteToSelectPet(memberId:String) {
        var member = memberRepo.findById(memberId)
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var now = sdf.parse(sdf.format(LocalDateTime.now()))
            for(i in 0 until member?.list!!.size){
                if(member.list.get(i).noticeEdt.after(now)){
                    member.list.remove(member.list.get(i))
                }
            }
        }

    /*************유저 ID로 최근 검색 조회**************/ //complete
    fun findToList(memberId:String): List<Pet> {
        var member = memberRepo.findById(memberId);
        var list :ArrayList<Pet> = ArrayList()
        for(i in 0 until member?.list!!.size){
            list.add(member.list.get(i))
        }
        var without = list.distinctBy { it.noticeNo }
        return without
    }
    fun addToLikes(memberId: String,noticeNo:String){
        var pet = petRepo.findByNoticeNo(noticeNo) //공고 번호로 해당 pet 찾기
        var like = likeRepo.findByMemberId(memberId)
        if(like ==null){
            var likes = Likes()
            likes.member = memberRepo.findById(memberId)!!
            likes.list.add(pet!!)
            likeRepo.save(likes)
        }
        else{
            like.list.add(pet!!)
        }
    }
    fun findToLikesList(memberId: String):List<Pet> { //like list 조회 == pet
        var like = likeRepo.findByMemberId(memberId)
        var list :ArrayList<Pet> = ArrayList()
        for(i in 0 until like?.list!!.size){
            list.add(like.list.get(i))
        }
        var without = list.distinctBy { it.noticeNo }
        return without
    }
}