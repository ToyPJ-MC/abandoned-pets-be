package Winter.pets.domain.jwt.handler

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        response?.status = 401;
        response?.writer?.write("OAuth Failer!")
    }
}

// 백엔드 /oauth/authorize/{provider-id}
// POST -> /oauth/authorize/kakao -> KAKO 로그인 폼
// 로그인 Success -> Authorization Code -> username, password => Credentials -> redirect-uri (oauth2/code/kakao?code={authorization_code}) -> accessToken