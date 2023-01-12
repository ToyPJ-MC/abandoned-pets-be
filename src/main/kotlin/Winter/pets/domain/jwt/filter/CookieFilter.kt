package Winter.pets.domain.jwt.filter

import Winter.pets.domain.jwt.KakaoService
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieFilter(private val kakaoService: KakaoService) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val cookie_response:HttpServletResponse = response as HttpServletResponse
        val cookie_request:HttpServletRequest = request as HttpServletRequest
        val access_token = cookie_response.getHeader("access_token")
        val refresh_token = cookie_response.getHeader("refresh_token")
        val servletPath = request.servletPath

        cookie_response.setHeader("Access-Control-Allow-Origin", "https://localhost:5173");
        cookie_response.setHeader("Access-Control-Allow-Credentials",  "true");

        if(servletPath.equals("/api/user/login")){ //login시 검증 안함
            if (chain != null) {
                chain.doFilter(request,cookie_response)
            }
        }
        if(access_token ==null)kakaoService.requestToToken()
        else if(refresh_token ==null)kakaoService.requestToToken()

        if (chain != null) {
            chain.doFilter(request, cookie_response)
        }
    }
}