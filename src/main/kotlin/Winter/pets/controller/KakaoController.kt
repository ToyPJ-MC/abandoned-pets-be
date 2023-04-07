package Winter.pets.controller

import Winter.pets.domain.jwt.kakao.KakaoService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Api(tags = ["Kakao"], description = "카카오 API")
@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {
   /* @Operation(summary = "인가코드로 토큰발급", description = "access,refresh 토큰 발급")
    @GetMapping("oauth")
    fun getToken(@RequestParam code: String ,request : HttpServletRequest):ResponseEntity<String>{
        val token =kakaoService.getToken(code)
        *//*val access_token = ResponseCookie.from("access_token",token.accessToken)
            .path("/")
            .maxAge(token.accessExpiresIn.toLong())
            .sameSite("NONE")
            .build()
        val refresh_token = ResponseCookie.from("refresh_token",token.refreshToken)
            .path("/")
            .maxAge(token.refreshExpiresIn.toLong())
            .sameSite("NONE")
            .build()*//*
//        var access_token = Cookie("access_token",token.accessToken)
//        access_token.maxAge = token.accessExpiresIn
//        access_token.path = "/"
//        var refresh_token = Cookie("refresh_token",token.refreshToken)
//        refresh_token.maxAge = token.refreshExpiresIn
//        refresh_token.path = "/"
//        response.addCookie(access_token)
//        response.addCookie(refresh_token)
        return ResponseEntity.ok().body(token)
    }
    @Operation(summary = "개인정보 불러오기", description = "카카오에서 발급한 access_token으로 개인정보 조회")
    @GetMapping("/kakao/info")
    fun postToUserInfo(@RequestParam("access_token")token :String):ResponseEntity<Any>{
        try{
            var list = kakaoService.getUserInfo(token)
            return ResponseEntity.ok().body(list)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }

    @Operation(summary = "인가코드 테스트", description = "")
    @GetMapping("/oauth/authorize")
    fun getCode(@RequestParam("code")code :String):ResponseEntity<Any>{
        try{
            println("$code")
            return ResponseEntity.ok().body(code)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }*/
}