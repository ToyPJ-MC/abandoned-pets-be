package Winter.pets.domain.jwt.service

import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.Pet
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@Service @EnableScheduling
class MemberService (
    private val memberRepo:MemberRepository){
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
}