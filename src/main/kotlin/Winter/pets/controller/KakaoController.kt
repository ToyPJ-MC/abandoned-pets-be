package Winter.pets.controller

import Winter.pets.domain.jwt.KakaoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CrossOrigin("http://192.168.0.16:8080")
@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {
    @Operation(summary = "카카오 로그인 시 토큰 발급 && 쿠키 저장")
    @PostMapping("/user/login")
    fun getToken(@RequestParam("code")code :String,response : HttpServletResponse):ResponseEntity<Any>{

        println("code = $code")
        val token =kakaoService.getToken(code)
        val ac = Cookie("accessToken",token.accessToken)
        ac.maxAge = token.accessExpiresIn
        ac.secure =true
        ac.isHttpOnly = true
       /* val access_cookie: ResponseCookie = ResponseCookie.from("acess_token", token.accessToken.toString())
            .maxAge(token.accessExpiresIn.toLong())
            .secure(true)
            .httpOnly(true)
            .build()*/
        response.addCookie(ac)
        response.setHeader("set_accessToken", ac.value)

        /*val refresh_cookie: ResponseCookie = ResponseCookie.from("refresh_token", token.refreshToken.toString())
            .maxAge(token.refreshExpiresIn.toLong())
            .secure(true)
            .httpOnly(true)
            .build()*/
        val re = Cookie("refreshToken",token.refreshToken)
        re.maxAge = token.refreshExpiresIn
        re.secure = true
        re.isHttpOnly = true
        response.addCookie(re)
        response.setHeader("set_refreshToken",re.value)

        val jwtToken = "NOT_AVAILABLE"

        return ResponseEntity.ok().body(jwtToken)
    }
    @Operation(summary = "access_token으로 유저 정보 요청")
    @GetMapping("/user/info")
    fun getUserinfo(request: HttpServletRequest):ResponseEntity<Any>{
        val cookie = request.cookies
        var token:String?=null
        for(i in 0 until cookie.size)
            if(cookie.get(i).name.equals("access_token"))token = cookie.get(i).value

        val getUser:HashMap<String,Any> = kakaoService.getUserInfo(token.toString()) as HashMap<String, Any>
        return ResponseEntity.ok().body(getUser)
    }
    @Operation(summary = "약관 정보")
    @PostMapping("/user/test")
    fun getUserAgree(request: HttpServletRequest):ResponseEntity<Any>{
       try{
           val cookie = request.cookies
           var token:String?=null
           for(i in 0 until cookie.size)
               if(cookie.get(i).name.equals("accessToken"))token = cookie.get(i).value

           val get = kakaoService.getAgreementInfo(token.toString())
           return ResponseEntity.ok().body(get)
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
            for(i in 0 until cookie.size)
                if(cookie.get(i).name.equals("access_token"))token = cookie.get(i).value
            kakaoService.userLogOut(token.toString())

            return ResponseEntity.ok().body("http://localhost:5173")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}