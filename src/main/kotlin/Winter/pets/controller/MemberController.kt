package Winter.pets.controller

import Winter.pets.domain.jwt.service.MemberService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@Api(tags = ["Member"], description = "멤버 API")
@RestController
@RequestMapping("api")
class MemberController(private val memberservice:MemberService) {

    @ApiOperation(value = "member id 로 최근 검색 조회", notes = "유저 id 입력하기")
    @GetMapping("/member/searchlist/memberid={member_id}")
    fun searchToList(@PathVariable("member_id")memberId:String):ResponseEntity<Any>{
        try{
            val list = memberservice.findToList(memberId)
            return ResponseEntity.ok().body(list)
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }

    @ApiOperation(value = "member id로 like 누르기", notes = "유저 id 입력하기")
    @GetMapping("/member/like/memberid={member_id}")
    fun addToLikePet(@PathVariable("member_id")memberId:String):ResponseEntity<Any>{
        try{
            return ResponseEntity.ok().body("")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
}