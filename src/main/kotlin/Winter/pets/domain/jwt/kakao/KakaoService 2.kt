package Winter.pets.domain.jwt.kakao

import Winter.pets.domain.jwt.domain.Member
import Winter.pets.domain.jwt.repository.MemberRepository
import org.json.JSONObject
import org.json.simple.parser.ParseException

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

@Service
class KakaoService(val kakaoProperties: KakaoProperties, val memberRepo : MemberRepository)
{

    /********* 인가로 토큰 받기 ************/
    fun getToken(code: String): String {
        val host = "https://kauth.kakao.com/oauth/token"
        var token=""
        val url = URL(host)
        val urlConnection = url.openConnection() as HttpURLConnection
        try{
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true

            val bw = BufferedWriter(OutputStreamWriter(urlConnection.outputStream))
            val sb = StringBuilder()
            sb.append("grant_type=authorization_code")
            sb.append("&client_id=${kakaoProperties.client_id}")
            sb.append("&redirect_uri=${kakaoProperties.redirect_uri}")
            sb.append("&code=$code")

            bw.write(sb.toString())
            bw.flush()

            val responseCode = urlConnection.responseCode
            println("$responseCode")

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line: String?=null
            var buf = StringBuffer()
            do {
                line = br.readLine()
                if (line != null) buf.append(line)
            } while (line != null)
            token = JSONObject(buf.toString()).getString("access_token")
        }catch (e : IOException){
            e.printStackTrace()
        }
        return token
    }
    /************ accessToken으로 userinfo 요청 ***************/
    fun getUserInfo(access_token : String):Member {
        var result = HashMap<String, Any>()
        var resultMember = Member()
        val host = "https://kapi.kakao.com/v2/user/me"
        if(memberRepo.findByRefreshToken(access_token)==null){
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
                if(find ==null){ //멤버를 새로 생성해야 할 떄
                    var member = Member()
                    member.name = nickname
                    member.email = setemail.toString()
                    member.profile = setpicture.toString()
                    member.refreshToken = access_token
                    resultMember = memberRepo.save(member)
                }
                else{ //엑세스토큰이 만료되고 새로 받은 경우
                    find.refreshToken = access_token
                    resultMember = memberRepo.save(find)
                }
                br.close()
                return resultMember
            } catch (e: IOException){
                e.printStackTrace()
            } catch (e : ParseException){
                e.printStackTrace()
            }
        }
        else{ //엑세스토큰이 유요한 경우
            var member = memberRepo.findByRefreshToken(access_token);
            result.put("nickname",member!!.name.toString())
            result.put("email",member!!.email.toString())
            result.put("picture",member!!.profile.toString())
            return member
        }
        return resultMember
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