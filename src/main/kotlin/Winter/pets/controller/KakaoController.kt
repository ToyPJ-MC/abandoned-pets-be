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
        println("$token")
        var accessToken = ResponseCookie.from("access_token", token.accessToken.toString())
            .maxAge(token.accessExpiresIn.toLong())
            .sameSite("None")
            .path("/")
            .domain("localhost")
            .build()
        var refreshToken = ResponseCookie.from("refresh_token", token.refreshToken.toString())
            .maxAge(token.refreshExpiresIn.toLong())
            .sameSite("None")
            .path("/")
            .domain("localhost")
            .build()
        var headers = HttpHeaders()
        headers.add(SET_COOKIE,"$accessToken")
        headers.add(SET_COOKIE,"$refreshToken")
        return ResponseEntity.ok().headers(headers).body("저장")
    }
    @Operation(summary = "개인정보 불러오기", description = "발급된 accessToken으로 요청")
    @PostMapping("/user/info")
    fun postToUserInfo(@RequestParam("access")token:String, request: HttpServletRequest):ResponseEntity<Any>{
        try{
           /* val cookie: Array<Cookie> = request.cookies
            var list=HashMap<String,Any>()
            for(i in 0 until cookie.size){
                if(cookie[i].name.equals("Set-Cookie")){
                    println(cookie[i].name)
                    list = kakaoService.getUserInfo(cookie[i].value) as HashMap<String, Any>
                }
            }*/
            //var list= kakaoService.getUserInfo(token
            val list = kakaoService.getUserInfo(token)
            return ResponseEntity.ok().body(list)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }
    @Operation(summary = "약관 정보")
    @PostMapping("/user/test")
    fun getUserAgree(request: HttpServletRequest):ResponseEntity<Any>{
       try{
           val cookies : Array<Cookie> = request.cookies
           var agree:String?=null
           for (i in 0 until cookies.size)
               if(cookies[i].name.equals("access_Token"))
                   agree = kakaoService.getAgreementInfo(cookies[i].value)

           return ResponseEntity.ok().body("test")
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