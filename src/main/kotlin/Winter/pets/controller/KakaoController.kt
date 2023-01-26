package Winter.pets.controller

import Winter.pets.domain.jwt.KakaoService
import io.swagger.v3.oas.annotations.Operation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {
    @Operation(summary = "카카오 로그인 시 토큰 발급 && 쿠키 저장")
    @PostMapping("/user/login")
    fun getToken(@RequestParam("code")code :String,response : HttpServletResponse):ResponseEntity<Any>{

        println("code = $code")
        val token =kakaoService.getToken(code)
        var accessToken = Cookie("access_token",token.accessToken)
        //accessToken.isHttpOnly = true
        accessToken.maxAge = token.accessExpiresIn
        accessToken.path="/"
        response.addCookie(accessToken)

        var refreshToken = Cookie("refresh_token",token.refreshToken)
        //refreshToken.isHttpOnly = true
        refreshToken.maxAge = token.refreshExpiresIn
        refreshToken.path = "/"
        response.addCookie(refreshToken)

        response.setHeader("Set-Cookie",refreshToken.value)

        return ResponseEntity.ok().body("저장")
    }
    @Operation(summary = "access_token으로 유저 정보 요청")
    @PostMapping("/user/info")
    fun getUserinfo(request: HttpServletRequest):ResponseEntity<Any>{
        try{
            val cookies : Array<Cookie> = request.cookies
            var agree:String?=null
            for (i in 0 until cookies.size)
                if(cookies[i].name.equals("accessToken"))
                    agree = kakaoService.getUserInfo(cookies[i].value).toString()
            return ResponseEntity.ok().body(agree)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "약관 정보")
    @PostMapping("/user/test")
    fun getUserAgree(request: HttpServletRequest):ResponseEntity<Any>{
       try{
           val cookies : Array<Cookie> = request.cookies
           var agree:String?=null
           for (i in 0 until cookies.size)
               if(cookies[i].name.equals("accessToken"))
                   agree = kakaoService.getAgreementInfo(cookies[i].value)
           return ResponseEntity.ok().body(agree)
       }catch (e : RuntimeException){
           return ResponseEntity.badRequest().body("잘못된 조회")
       }
    }
    @Operation(summary = "로그 아웃")
    @PostMapping("/user/logout")
    fun getUserLogOut(request: HttpServletRequest):ResponseEntity<Any>{
        try{
            val cookie = request.cookies
            var token:String?=null
            for(i in 0 until cookie.size){
                if(cookie.get(i).name.equals("access_token"))token=cookie.get(i).value
            }
            kakaoService.userLogOut(token.toString())

            return ResponseEntity.ok().body("http://localhost:5173")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}