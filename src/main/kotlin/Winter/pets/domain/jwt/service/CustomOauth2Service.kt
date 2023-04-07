package Winter.pets.domain.jwt.service

import Winter.pets.domain.jwt.domain.Member
import Winter.pets.domain.jwt.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.servlet.http.HttpSession

@Service
@Transactional
class CustomOauth2Service(private var memberRepository: MemberRepository) : DefaultOAuth2UserService() {
    private lateinit var httpSession: HttpSession
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        var oAuth2User = super.loadUser(userRequest)
        var attributes:Map<String,Any> = oAuth2User.attributes

        var kakao_account:Map<String,Any> = attributes["kakao_account"] as Map<String,Any>
        var email:String = kakao_account["email"] as String
        var properties:Map<String,Any> = attributes["properties"] as Map<String,Any>
        var nickname:String = properties["nickname"] as String
        var profile:String = properties["profile_image"] as String

        if(memberRepository.findByEmail(email)==null){
            var member = Member()
            member.profile = profile
            member.name = nickname
            member.email = email
            member.role = "ROLE_USER"
            memberRepository.save(member)
        }
        println("$email")
        return DefaultOAuth2User(Collections.singleton(SimpleGrantedAuthority("ROLE_USER")),attributes,"id")
    }
}