package Winter.pets.domain.jwt

import org.json.JSONObject
import org.json.simple.parser.JSONParser
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
import java.util.Objects
import javax.imageio.IIOException


@Service
class KakaoService {
    /********* 인가로 토큰 받기 ************/
    fun getToken(code : String):HashMap<String,String>{
        val host = "https://kauth.kakao.com/oauth/token"
        val url = URL(host)
        val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
        var token = HashMap<String,String>()
        try{
            urlConnection.requestMethod ="POST"
            urlConnection.doOutput = true
            val bw = BufferedWriter(OutputStreamWriter(urlConnection.outputStream))
            val sb:StringBuilder = StringBuilder()
            sb.append("grant_type=authorization_code")
            sb.append("&client_id=2aad40910868e3c5fa9594f8de34a07b")
            sb.append("&redirect_uri=http://localhost:8080/member/kakao")
            sb.append("&code=\" + code")

            bw.write(sb.toString())
            bw.flush()

            val responseCode:Int = urlConnection.responseCode
            println("$responseCode")

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line:String
            var buf = StringBuffer()
            do{
                line = br.readLine()
                if(line != null) buf.append(line)
            }while (line != null)
            println("$buf")
            val root = JSONObject(buf.toString())
            val access_token= root.getJSONObject("access_tocken").toString()
            val refresh_tocken= root.getJSONObject("refresh_tocken").toString()
            println("access_token = $access_token")
            println("refresh_token = $refresh_tocken")
            token.put("access_token",access_token)
            token.put("refresh_tocken",refresh_tocken)
            br.close()
            bw.close()

        }catch (e:IIOException){
            e.printStackTrace()
        }catch (e:ParseException){
            e.printStackTrace()
        }
        return token
    }
    fun getUserInfo(access_token : String):Map<String,Any> {
        val host = "https://kapi.kakao.com/v2/user/me"
        var result = HashMap<String, Any>()
        try {
            val url = URL(host)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token)
            urlConnection.requestMethod = "GET"

            val responseCode = urlConnection.responseCode
            println("responseCode = $responseCode")

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line: String
            var buf = StringBuffer()
            do {
                line = br.readLine()
                if (line != null) buf.append(line)
            } while (line != null)
            println("$buf")
            val root = JSONObject(buf.toString())
            val kakao_account = root.getJSONObject("kakap_account").toString()
            val properties = root.getJSONObject("properties").toString()
            val id = root.getJSONObject("id").toString()
            val nickname = root.getJSONObject("nickname").toString()
            val age_range = root.getJSONObject("age_range").toString()
            val sex = root.getJSONObject("sex").toString()

            result.put("id", id)
            result.put("nickname", nickname)
            result.put("age_range", age_range)

            br.close()

        } catch (e: IOException){
            e.printStackTrace()
        }catch (e : ParseException){
            e.printStackTrace()
        }
        return result
    }
    fun getAgreementInfo(access_token : String):String{
        var result=""
        val host = "https://kapi.kakao.com/v2/user/scopes"
        try{
            val url = URL(host)
            val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.setRequestProperty("Authorization","Bearer "+access_token)

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line:String
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
}