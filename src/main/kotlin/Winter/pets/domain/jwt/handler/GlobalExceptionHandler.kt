package Winter.pets.domain.jwt.handler

import com.nimbusds.oauth2.sdk.ErrorResponse
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RestController
class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleJwtExpiredException(e: ExpiredJwtException): Any {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었습니다.")
    }
}