package Winter.pets.controller

import Winter.pets.domain.jwt.provider.JwtProvider
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
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Api(tags = ["Member"], description = "멤버 API")
@RestController
@RequestMapping("api")
class MemberController(private val memberservice:MemberService,
                       private val jwtProvider: JwtProvider) {

    @ApiOperation(value = "member 별 최근 검색 조회 기능", notes = "유저 access token 입력, 만료시 403에러 발생")
    @GetMapping("/member/searchlist")
    fun searchToList(@RequestParam("access_token")token:String): Any {
        return memberservice.findToList(token)
    }
    @ApiOperation(value = "member 별 최근조회 list 삭제 기능", notes = "유저 access token 입력, 만료시 403에러 발생")
    @PostMapping("/member/delete/searchlist")
    fun deleteToSearchList(@RequestParam("access_token")token:String,@RequestParam(value="noticeNo") noticeNo: List<String>): Any {
        return memberservice.deleteToSelectPet(token,noticeNo)
    }
    @ApiOperation(value = "member 별 좋아요 기능", notes = "유저 access token 입력, 만료시 403에러 발생")
    @PostMapping("/member/like")
    fun addToLikePet(@RequestParam("access_token")token:String,noticeNo:String):Any{
         return memberservice.addToLikes(token,noticeNo)
    }
    @ApiOperation(value = "member 별 좋아요 List 조회 기능", notes = "유저 access token 입력, 만료시 403에러 발생")
    @GetMapping("/member/likelist")
    fun findToLikeList(@RequestParam("access_token")token:String):Any{
        return memberservice.findToLikesList(token)
    }
    @ApiOperation(value = "member 별 좋아요 List 삭제 기능", notes = "유저 access token 입력, 만료시 403에러 발생")
    @PostMapping("/member/delete/likelist")
    fun deleteToLikeList(@RequestParam("access_token")token:String,@RequestParam(value="noticeNo") noticeNo: List<String>):Any{
        return memberservice.deleteLikePet(token,noticeNo)

    }
    @ApiOperation(value = "member 로그아웃 기능", notes = "발급된 access token 입력")
    @PostMapping("/member/logout")
    fun memberLogOut(@RequestParam("access_token") token:String):ResponseEntity<Any>{
        try{
            memberservice.memberLogOut(token)
            return ResponseEntity.ok().build()
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body(e.printStackTrace())
        }
    }

    @ApiOperation(value = "member 토큰 발급", notes = "로그인 시 최초 사용 및, access token 만료 시 재발급")
    @GetMapping("/member/token/new")
    fun memberNewToken(@RequestParam("refresh_token")token:String):Any{
        return memberservice.memberNewToken(token)
    }

    /*@ApiOperation(value = "access token 만료 시 재발급", notes = "access token 검증")
    @GetMapping("/member/token/")
    fun memberTokenValidate(@RequestParam("access_token") token:String): Any? {
        try{
            memberservice.memberNewToken(token)
            return ResponseEntity.ok().body("유효한 토큰입니다.")
        }catch (e:RuntimeException){
            return ResponseEntity.status(403)
        }
    }*/
    @ApiOperation(value = "member info 조회", notes = "member access_token 사용")
    @GetMapping("/member/info")
    fun postToUserInfo(@RequestParam("access_token")token :String):Any{
        return memberservice.memberInfo(token);
    }

}