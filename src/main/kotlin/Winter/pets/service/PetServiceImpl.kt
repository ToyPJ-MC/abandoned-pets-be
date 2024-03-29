package Winter.pets.service

import Winter.pets.domain.Country.Center
import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.jwt.domain.Member
import Winter.pets.domain.jwt.provider.JwtProvider
import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.Pet
import Winter.pets.repository.*
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

@Service
class PetServiceImpl(
    private val addPetRepo: AddToPetRepository,
    private val memberRepo : MemberRepository,
    private val jwtProvider: JwtProvider
) : PetService {


    //저장된 유기동물 전체 조회시 paging 통해 오래된 동물부터
    override fun findToPet(page: Int, size: Int): List<Pet> {
        var request: PageRequest = PageRequest.of(page, size, Sort.by("happenDt").ascending())
        var findList: List<Pet> = addPetRepo.findAll(request).content
        return findList
    }
    //******************************* db Max Page 조회****************************//
    override fun findToMaxPage(): String {
        var findPage = addPetRepo.findAll().size
        var size = findPage
        return (size/6).toString()
    }

    override fun deleteToSearchList(memberid: String) {
        TODO("Not yet implemented")
    }
    //전체 페이지 조회
    override fun allToPet(): String {
        val size = addPetRepo.findAll().size -1
        return size.toString()
    }
    override fun selectToPet(token: String, kindCd: String, careNm: String,orgNm : String,neuterYn : String):Any {
        var list = ArrayList<Pet>()
        if(token.equals("No")){
            var pet: List<Pet>? = addPetRepo.findByOrgNmAndNeuterYnAndAndKindCd(orgNm,neuterYn,kindCd);
            if(pet ==null){
                return ResponseEntity.badRequest().body("해당 동물이 존재하지 않습니다.")
            }
            else{
                for(i in 0 until pet.size){
                    list.add(pet[i])
                }
                return list
            }

        }else{
            if(jwtProvider.validateToken(token)){
                var email = jwtProvider.getEmail(token)
                var member = memberRepo.findByEmail(email)
                var pet:List<Pet>? = addPetRepo.findByOrgNmAndNeuterYnAndAndKindCd(orgNm,neuterYn,kindCd);
                if(pet == null){
                    return ResponseEntity.badRequest().body("해당 동물이 존재하지 않습니다.")
                }
                else{
                    for(i in 0 until pet.size){
                        list.add(pet.get(i))
                        if(!member!!.list.contains(pet.get(i))){
                            member!!.list.add(pet.get(i))
                            memberRepo.save(member)
                        }
                    }
                    return list
                }
            }
            else return ResponseEntity.status(403)
        }

    }
}