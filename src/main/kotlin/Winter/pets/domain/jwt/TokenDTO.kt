package Winter.pets.domain.jwt

import lombok.NoArgsConstructor

@NoArgsConstructor
class TokenDTO {
    var tokenType:String?=null
    var accessToken:String?=null
    var refreshToken:String?=null
    var accessExpiresIn:Int = 0
    var refreshExpiresIn:Int =0
    var profilesPic:String?=null
}