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
            urlConnection.requestMethod ="POST"
            urlConnection.doOutput = true
            val bw = BufferedWriter(OutputStreamWriter(urlConnection.outputStream))
            val sb:StringBuilder = StringBuilder()
            sb.append("grant_type=authorization_code")
            sb.append("&client_id=",kakaoProperties.client_id)
            sb.append("&redirect_uri=http://localhost:5173")
            //sb.append("&redirect_uri=",kakaoProperties.redirect_uri)
            sb.append("&code=" + code)
            sb.append("&client_secret=",kakaoProperties.client_secret)
            bw.write(sb.toString())
            bw.flush()

            val responseCode:Int = urlConnection.responseCode
            println("$responseCode")

            val br= BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line:String?=null
            var buf = StringBuffer()
            do{
                line = br.readLine()
                if(line != null) buf.append(line)
            }while (line != null)
            val root = JSONObject(buf.toString())
            println("$root")
            token.accessToken = root.get("access_token").toString()
            token.accessExpiresIn = root.get("expires_in") as Int
            token.refreshToken = root.get("refresh_token").toString()
            token.refreshExpiresIn = root.get("refresh_token_expires_in") as Int
            token.tokenType = root.get("token_type").toString()

            br.close()
            bw.close()

        }catch (e:IIOException){
            e.printStackTrace()
        }
        return token
    }
    /************ accessToken으로 userinfo 요청 ***************/
    fun getUserInfo(access_token : String):Map<String,Any> {
        val host = "https://kapi.kakao.com/v2/user/me"
        var result = HashMap<String, Any>()
        var member = Member()
        try {
            val url = URL(host)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token)
            urlConnection.requestMethod = "GET"

            val responseCode = urlConnection.responseCode
            println("responseCode = $responseCode")

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
            val email = kakaoAcount.get("email")
            val gender = kakaoAcount.get("gender")
            val picture = kakaoAcount.getJSONObject("profile").get("thumbnail_image_url").toString()
            val id = root.get("id")
            result.put("id",id)
            result.put("nickname", nickname)
            result.put("gender", gender)
            result.put("age_range", email)
            result.put("picture", picture)
            br.close()
        } catch (e: IOException){
            e.printStackTrace()
        }catch (e : ParseException){
            e.printStackTrace()
        }
        if(memberRepo.findByEmail(result.get("email").toString())==null){
            val member= Member()
            member.email=result.get("age_range").toString()
            member.gender=result.get("gender").toString()
            member.name = result.get("nickname").toString()
            member.id = result.get("id") as Long?
            memberRepo.save(member)
        }
        return result
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
    /********* 로그 아웃 시 ************/
    fun userLogOut(access_token: String):Unit{
        val host = "https://kapi.kakao.com/v1/user/logout"
        try {
            val url = URL(host)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token)
            urlConnection.requestMethod = "POST"
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
    fun requestToToken():String{
        return "http://localhost:5173/login"
    }
}