package Winter.pets.domain.jwt.handler

import Winter.pets.domain.jwt.provider.JwtProvider
import Winter.pets.domain.jwt.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    @Autowired
    private lateinit var jwtProvider: JwtProvider
    @Autowired
    private lateinit var memberRepository: MemberRepository


    // /login/oauth2/code/{provider-id} -> OAuth 로그인 끝난 후 login/oauth2/code/kakao -> OAuth2AuthenticationSuccessHandler
    @Throws(Exception::class)
    override fun onAuthenticationSuccess(request : HttpServletRequest, response : HttpServletResponse, authentication : Authentication){
        var member = authentication.principal as OAuth2User

        var kakakProperty = member.attributes.get("kakao_account") as Map<String,Any>
        var savedEmail = kakakProperty.get("email") as String
        var roles = member.authorities.toString()

        var findMember = memberRepository.findByEmail(savedEmail)

        var refreshToken = jwtProvider.createRefreshToken(roles, savedEmail)
        findMember!!.refreshToken = refreshToken
        memberRepository.save(findMember)
        println(refreshToken)
        if(response.isCommitted){
            throw IllegalStateException("Response has already been committed")
            return
        }
        redirectStrategy.sendRedirect(request, response, "http://localhost:5173/oauth2/redirect?code=${refreshToken}")
    }
}