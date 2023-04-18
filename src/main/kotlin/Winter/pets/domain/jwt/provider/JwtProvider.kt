package Winter.pets.domain.jwt.provider

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.streams.toList

@Component
class JwtProvider {
    @Value("\${jwt.secret}")
    private lateinit var secretKey : String
    @Value("\${jwt.authorities_key}")
    private lateinit var AUTHORITIES_KEY : String
    @Value("\${jwt.expire_in_seconds}")
    private var validityInMilliseconds : Long =3600

    private fun getSecretKey() : Key {
        var keyBytes = Base64.getDecoder().decode(secretKey).toUByteArray() // base64 decode
        return Keys.hmacShaKeyFor(keyBytes.toByteArray())
    }

    fun createToken(roles : String, email : String): String {
        var now = Date()
        var validity = Date(now.getTime() + validityInMilliseconds * 1000) // 1시간
        var key = getSecretKey();
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim(AUTHORITIES_KEY,roles)
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
            .compact()
    }
    fun createRefreshToken(roles: String, email: String):String{
        var now = Date()
        var validity = Date(now.getTime() + validityInMilliseconds * 1000*24*30) // 30일
        var key = getSecretKey();
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim(AUTHORITIES_KEY,roles)
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
            .compact()
    }
    fun validateToken(token : String?): Boolean {
        try{
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
            return true
        }catch (e : SecurityException){ // signature 에러
            throw SecurityException("Invalid JWT signature")

        }catch (e : MalformedJwtException){ // token 에러
            throw MalformedJwtException("Invalid JWT token")
        }catch (e : ExpiredJwtException){ // 만료 에러
            throw ExpiredJwtException(null, null, "Expired JWT token")
        }catch (e : UnsupportedJwtException){ // 지원하지 않는 에러
            throw UnsupportedJwtException("Unsupported JWT token")
        }catch (e : IllegalArgumentException){ // 토큰이 비어있는 에러
            throw IllegalArgumentException("JWT claims string is empty")
        }
        return false
    }

    fun getExpiredTokenClaims(token: String):Claims{
        try{
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
        }catch (e : ExpiredJwtException){
            return e.claims
        }
        return null as Claims
    }

    fun getAuthentication(token : String?): Authentication {
        var claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .body
        val authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",").toTypedArray())
            .map{SimpleGrantedAuthority(it)}
            .toList()
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }
    fun getEmail(token : String?): String {
        var claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .body
        return claims.subject
    }
}