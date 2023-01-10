package Winter.pets.controller

import Winter.pets.domain.jwt.KakaoService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@CrossOrigin("http://192.168.0.16:8080")
@RequestMapping("/api")
@RestController
class KakaoController(private val kakaoService: KakaoService) {

    @GetMapping("/kakao")
    fun getCi(@RequestParam("code")code :String):HashMap<String,String>{
        println("code = $code")
        val token =kakaoService.getToken(code)
        val userinfo :HashMap<String,Any> = kakaoService.getUserInfo(token.get("access_token").toString()) as HashMap<String, Any>
        userinfo.put("access_token",token.get("access_token").toString())
        return token
    }
}