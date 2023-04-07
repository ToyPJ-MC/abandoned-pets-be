package Winter.pets.controller

import Winter.pets.domain.jwt.service.MemberService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
@Api(tags = ["Member"], description = "멤버 API")
@RestController
@RequestMapping("api")
class MemberController(private val memberservice:MemberService) {

    @ApiOperation(value = "member id 로 최근 검색 조회", notes = "유저 access_token 입력하기")
    @GetMapping("/member/searchlist/memberid={access_token}")
    fun searchToList(@PathVariable("access_token")token:String):ResponseEntity<Any>{
        try{
            val list = memberservice.findToList(token)
            return ResponseEntity.ok().body(list)
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }

    @ApiOperation(value = "member id로 like 누르기", notes = "유저 id 입력하기")
    @PostMapping("/member/like/memberid={member_id}")
    fun addToLikePet(@PathVariable("member_id")memberId:String,noticeNo:String):ResponseEntity<Any>{
        try{
            memberservice.addToLikes(memberId,noticeNo)
            return ResponseEntity.ok().body("success")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
    @ApiOperation(value = "member id로 like List 조회", notes = "유저 id 입력하기")
    @GetMapping("/member/like/list/memberid={member_id}")
    fun findToLikeList(@PathVariable("member_id")memberId:String):ResponseEntity<Any>{
        try{
            val list = memberservice.findToLikesList(memberId)
            return ResponseEntity.ok().body(list)
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
    @ApiOperation(value = "member id별 최근조회 list 삭제 기능", notes = "유저 id , 공고 번호 입력하기")
    @PostMapping("/member/searchlist/delete/memberid={member_id}")
    fun deleteToSearchList(@PathVariable("member_id")memberId:String,@RequestBody noticeNo: List<String>):ResponseEntity<Any>{
        try{
            memberservice.deleteToSelectPet(memberId,noticeNo)
            return ResponseEntity.ok().body(noticeNo + "삭제 완료")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
}