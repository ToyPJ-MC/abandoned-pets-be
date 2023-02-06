package Winter.pets.controller

import Winter.pets.domain.jwt.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class MemberController(private val memberservice:MemberService) {

    @Operation(summary = "유저 email로 최근 검색 조회", description = "유저 email 입력하기")
    @GetMapping("user/select")
    fun searchToList(@RequestParam("User-Email")userEmail:String):ResponseEntity<Any>{
        try{
            val list = memberservice.findToList(userEmail)
            return ResponseEntity.ok().body(list)
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
}