package Winter.pets.controller

import Winter.pets.domain.jwt.KakaoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseCookie

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {
    @Operation(summary = "AccessToken,RefreshToken 발급하기", description = "로그인 시 발급된 인가코드 입력")
    @GetMapping("/user/login")
    fun getToken(@RequestParam("code")code :String,response: HttpServletResponse):ResponseEntity<String>{

        val token =kakaoService.getToken(code)
        val list = kakaoService.getUserInfo(token.accessToken.toString())

        return ResponseEntity.ok().body(list.get("id").toString())
    }
    @Operation(summary = "개인정보 불러오기", description = "개인 고유 Id로 정보 요청")
    @GetMapping("/user/info")
    fun postToUserInfo(@RequestParam("member_id")memberId : String):ResponseEntity<Any>{
        try{
            val member =kakaoService.memberInfo(memberId);
            return ResponseEntity.ok().body(member)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
}