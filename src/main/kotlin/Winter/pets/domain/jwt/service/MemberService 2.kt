package Winter.pets.domain.jwt.service

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.jwt.provider.JwtProvider
import Winter.pets.domain.jwt.repository.LikeRepository
import Winter.pets.domain.jwt.repository.MemberRepository
import Winter.pets.domain.kind.Pet
import Winter.pets.repository.AddToPetRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service
class MemberService (
    private val memberRepo:MemberRepository,
    private val likeRepo: LikeRepository,
    private val petRepo : AddToPetRepository,
    private val jwtProvider: JwtProvider
){
    //좋아요 list 삭제 기능
    fun deleteLikePet(token: String, noticeNo:List<String>):Any {
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var email = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(email)
            var likes = likeRepo.findByMember(member!!)
            var size = likes!!.list.size
            for(i in 0 until noticeNo.size){
                var k=0
                while(k<size){
                    if(likes.list.get(k).noticeNo.equals(noticeNo.get(i))){
                        likes.list.remove(likes.list.get(k))
                        size--
                    }
                    k++
                }
            }
            likeRepo.save(likes)
            return ResponseEntity.ok().body("삭제 성공")
        }else return ResponseEntity.status(403)
    }
    //최근 조회 삭제 기능
   fun deleteToSelectPet(token: String, noticeNo:List<String>):Any {
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var email = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(email)
            var size = member!!.list.size
            for(i in 0 until noticeNo.size){
                var k=0
                while(k<size){
                    if(member.list.get(k).noticeNo.equals(noticeNo.get(i))){
                        member.list.remove(member.list.get(k))
                        size--
                    }
                    k++
                }
            }
            memberRepo.save(member)
            return ResponseEntity.ok().body("삭제 성공")
        }
        else return ResponseEntity.status(403)
        }

    /*************유저 토큰으로 최근 검색 조회**************/ //complete
    fun findToList(token:String): Any {
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var getEmail = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(getEmail)
            var list :ArrayList<Pet> = ArrayList()
            for(i in 0 until member?.list!!.size){
                list.add(member.list.get(i))
            }
            var without = list.distinctBy { it.noticeNo }
            return without
        }
        else return ResponseEntity.status(403)
    }
    /************ member like 기능 **************/
    fun addToLikes(token: String,noticeNo:String):Any{
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var getEmail = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(getEmail)
            var pet = petRepo.findByNoticeNo(noticeNo) //공고 번호로 해당 pet 찾기
            var likes:Likes? = likeRepo.findByMember(member!!)
            for(i in 0 until likes!!.list.size){
                if(!!likes.list.get(i).noticeNo.equals(noticeNo)){
                    return ResponseEntity.badRequest().body("이미 좋아요된 유기 동물입니다.")
                }
                else{
                    if(likes == null){ //like가 없을때
                        var like = Likes()
                        like.member = memberRepo.findByEmail(getEmail)!!
                        like.list.add(pet!!)
                        likeRepo.save(like)
                    }
                    else{ //like가 있을때
                        likes.list.add(pet!!)
                        likeRepo.save(likes)
                    }
                }
            }
            return ResponseEntity.ok().body("좋아요 성공")
        }
        else return ResponseEntity.status(403)

    }
    /************ like 목록 조회 **************/
    fun findToLikesList(token: String):Any { //like list 조회 == pet
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var email = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(email)
            var like:Likes? = likeRepo.findByMember(member!!)
            var list :ArrayList<Pet> = ArrayList()
            for(i in 0 until like?.list!!.size){
                list.add(like.list.get(i))
            }
            var without = list.distinctBy { it.noticeNo }
            return without
        }
        else return ResponseEntity.status(403)
    }
    // 토큰 재발급 및, 로그인 시 refresh 토큰으로 재발급
    fun memberNewToken(token:String):Any{ //access token 발급 및 재발급 시 refresh 토큰 받음
        var map = HashMap<String,String>()
        var email = jwtProvider.getEmail(token)
        var member = memberRepo.findByEmail(email)
        if(jwtProvider.validateToken(member!!.refreshToken)){
            member!!.refreshToken = jwtProvider.createRefreshToken(member.role,email)
            member!!.accessToken = jwtProvider.createToken(member.role,email)
            map.put("accessToken",member.accessToken!!)
            map.put("refreshToken",member.refreshToken!!)
            memberRepo.save(member)
            return ResponseEntity.ok().body(map)
            }
        else return ResponseEntity.status(403)
    }
    // 로그아웃 기능 - 로그아웃 시 토큰 null
    fun memberLogOut(token:String){
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var email = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(email)
            member!!.refreshToken = null
            member!!.accessToken = null
            memberRepo.save(member)
        }
    }
    //멤버 개인 정보 조회 기능
    fun memberInfo(token: String):Any{
        var member = memberRepo.findByAccessToken(token)
        if(jwtProvider.validateToken(token)&&member?.accessToken.equals(token)){
            var email = jwtProvider.getEmail(token)
            var member = memberRepo.findByEmail(email)
            return ResponseEntity.ok().body(member)
        }
        else return ResponseEntity.status(403)
    }
}