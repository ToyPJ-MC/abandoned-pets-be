package Winter.pets.domain.jwt.service

import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.SelectPets
import Winter.pets.repository.PetsRepository
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MemberService (
    private val memberRepo:MemberRepository,
    private val petRepo:PetsRepository){
    /************최근 검색 조회 db 매주 월요일 12시 05분 delete 기능****************/
    @Scheduled(cron="0 05 12 * JAN *") //순서별로 초 분 시 일 월 요일 년(생략가능)
   fun deleteToSelectPet() {
        val select = petRepo.findAll(Sort.by(Sort.Direction.DESC,"createAt"))
        for(i in 0 until select.size){
            if(select[i].createAt.isAfter(LocalDateTime.now())){
                petRepo.delete(select[i])
            }
        }
    }
    /*************유저 Email로 최근 검색 조회**************/
    fun findToList(userEmail:String): List<SelectPets> {
       var member = memberRepo.findByEmail(userEmail)
        if (member != null) {
            println("${member.list}")
            return member.list
        }
        return emptyList()
    }
}