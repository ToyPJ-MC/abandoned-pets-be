package Winter.pets.domain.jwt

import Winter.pets.domain.jwt.domain.Member
import Winter.pets.domain.jwt.repository.MemberRepository
import org.json.JSONObject
import org.json.simple.parser.ParseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.imageio.IIOException

@Service
class KakaoService(val kakaoProperties: KakaoProperties,val memberRepo : MemberRepository)
{

    /********* 인가로 토큰 받기 ************/
    fun getToken(code : String):TokenDTO{
        val host = "https://kauth.kakao.com/oauth/token"
        val url = URL(host)
        val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
        val token= TokenDTO()
        try{

            val postData = "grant_type=authorization_code&client_id=${kakaoProperties.client_id}&redirect_uri=${kakaoProperties.redirect_uri}&code=${code}"
            val postDataBytes = postData.toByteArray(StandardCharsets.UTF_8)
            val conn = URL(host).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            conn.setRequestProperty("Content-Length", postDataBytes.size.toString())
            conn.outputStream.write(postDataBytes)

            val response = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(response)

            token.accessToken = root.get("access_token").toString()
            token.accessExpiresIn = root.get("expires_in") as Int
            token.refreshToken = root.get("refresh_token").toString()
            token.refreshExpiresIn = root.get("refresh_token_expires_in") as Int
            token.tokenType = root.get("token_type").toString()

        }catch (e:IIOException){
            e.printStackTrace()
        }
        return token
    }
    /************ accessToken으로 userinfo 요청 ***************/
    fun getUserInfo(access_token : String):Map<String,Any> {
        var result = HashMap<String, Any>()
        if(memberRepo.findByAccessToken(access_token)==null){
            val host = "https://kapi.kakao.com/v2/user/me"
            try {
                val url = URL(host)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConnection.setRequestProperty("Authorization", "Bearer " + access_token)
                urlConnection.requestMethod = "GET"

                val responseCode = urlConnection.responseCode

                val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
                var line: String?=null
                var buf = StringBuffer()
                do {
                    line = br.readLine()
                    if (line != null) buf.append(line)
                } while (line != null)
                val root = JSONObject(buf.toString())
                val kakaoAcount = root.getJSONObject("kakao_account")
                val nickname = kakaoAcount.getJSONObject("profile").get("nickname").toString()
                val setemail = kakaoAcount.get("email")
                val setpicture = kakaoAcount.getJSONObject("profile").get("thumbnail_image_url")
                val setid = root.get("id")
                result.put("id",setid)
                result.put("nickname", nickname)
                result.put("email", setemail)
                result.put("picture", setpicture)


                var find = memberRepo.findById(setid.toString())
                if(!find?.accessToken.equals(access_token)){
                    var member= Member()
                    member.profile = setpicture.toString()
                    member.name = nickname
                    member.email =setemail.toString()
                    member.id = setid.toString()
                    member.accessToken = access_token
                    memberRepo.save(member)
                }
                br.close()
            } catch (e: IOException){
                e.printStackTrace()
            }catch (e : ParseException){
                e.printStackTrace()
            }
            return result
        }
        else{ //엑세스토큰이 유요한 경우
            var member = memberRepo.findByAccessToken(access_token);
            result.put("id", member?.id!!.toLong())
            result.put("nickname",member.name.toString())
            result.put("email",member.email.toString())
            result.put("picture",member.profile.toString())
            return result
        }

    }
    /******** 약관 정보 ************/
    fun getAgreementInfo(access_token : String):String{
        var result=""
        val host = "https://kapi.kakao.com/v2/user/scopes"
        try{
            val url = URL(host)
            val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.setRequestProperty("Authorization","Bearer "+access_token)

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line:String?=null
            var buf = StringBuffer()
            do{
                line = br.readLine()
                if(line != null) buf.append(line)
            }while (line != null)
            result=buf.toString()
            val responseCode = urlConnection.responseCode
            println("responseCode = $responseCode")
            br.close()
        }catch (e : IOException){
            e.printStackTrace()
        }catch (e : ProtocolException){
            e.printStackTrace()
        }catch (e : MalformedURLException){
            e.printStackTrace()
        }
        return result
    }

    fun memberInfo(memberId : String): Member? {
        return memberRepo.findById(memberId);
    }
}