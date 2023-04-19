package Winter.pets.domain.jwt.filter

import Winter.pets.domain.jwt.provider.JwtProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JwtFilter(private val jwtProvider: JwtProvider) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val jwt:String? = resolveToken(httpServletRequest)
        //유효성 검증
        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            // 토큰에서 email, 권한을 뽑아 스프링 시큐리티 user를 만들어 Authentication 반환
            val authentication = jwtProvider.getAuthentication(jwt)
            // 해당 스프링 시큐리티 유저를 시큐리티 컨텍스트에 저장, 즉 디비를 거치지 않음
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain?.doFilter(httpServletRequest, response)
    }

    private fun resolveToken(httpServletRequest: HttpServletRequest): String? {
        val bearerToken = httpServletRequest.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length)
        }
        return null
    }
}


