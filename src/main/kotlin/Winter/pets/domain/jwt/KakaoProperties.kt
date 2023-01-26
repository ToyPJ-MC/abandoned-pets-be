package Winter.pets.domain.jwt

import lombok.Builder
import lombok.NoArgsConstructor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kakao")
data class KakaoProperties
    ( val client_id:String,
      val client_secret:String,
      val redirect_uri:String
      ) {

}