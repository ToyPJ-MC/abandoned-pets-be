package Winter.pets.domain.jwt.handler

import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
@Component
class JwtAccessDeniedHandler : AccessDeniedHandler{
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException
    ) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN) //필요한 권한이 없이 접근할 때 403
    }
}