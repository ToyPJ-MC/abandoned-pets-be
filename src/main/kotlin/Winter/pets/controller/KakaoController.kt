package Winter.pets.controller

import Winter.pets.domain.jwt.KakaoService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseCookie

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.CookieHandler
import java.net.CookieManager
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
@Api("카카오 로그인")
@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {
    @Operation(summary = "인가코드로 토큰발급", description = "access,refresh 토큰 발급")
    @GetMapping("/user/login")
    fun getToken(@RequestParam("code")code :String,response: HttpServletResponse):ResponseEntity<String>{
        println("$code")
        val token =kakaoService.getToken(code)
        /*val access_token = ResponseCookie.from("access_token",token.accessToken)
            .path("/")
            .maxAge(token.accessExpiresIn.toLong())
            .sameSite("NONE")
            .build()
        val refresh_token = ResponseCookie.from("refresh_token",token.refreshToken)
            .path("/")
            .maxAge(token.refreshExpiresIn.toLong())
            .sameSite("NONE")
            .build()*/
        var access_token = Cookie("access_token",token.accessToken)
        access_token.maxAge = token.accessExpiresIn
        access_token.path = "/"
        var refresh_token = Cookie("refresh_token",token.refreshToken)
        refresh_token.maxAge = token.refreshExpiresIn
        refresh_token.path = "/"
        response.addCookie(access_token)
        response.addCookie(refresh_token)
        return ResponseEntity.ok().body("헤더 저장완료")

    }
    @Operation(summary = "개인정보 불러오기", description = "카카오에서 발급한 access_token으로 개인정보 조회")
    @GetMapping("/user/info")
    fun postToUserInfo(@RequestParam("access_token")token :String):ResponseEntity<Any>{
        try{
            var list = kakaoService.getUserInfo(token)
            return ResponseEntity.ok().body(list)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
}