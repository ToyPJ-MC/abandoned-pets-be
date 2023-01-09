package Winter.pets.service

import Winter.pets.domain.Country.Center
import Winter.pets.domain.Country.GunGu
import Winter.pets.repository.CenterRepository
import Winter.pets.repository.gunguRepository
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
@Service
class CenterServiceImpl(private val centerRepo: CenterRepository,
                        private val gunguRepo : gunguRepository) : CenterService {
    /*******시 군 구에 대한 보호소 조회 기능*********/
    override fun findToCenter(siName: String, gunguName: String): List<String> {
        val findCenter: List<Center> = centerRepo.findBySiNameAndGunguName(siName, gunguName);
        val list = ArrayList<String>()
        for (i in 0 until findCenter.size) {
            list.add(findCenter.get(i).centerName.toString())
        }
        return list
    }
    /**********시 군 구에 대한 보호소 저장 기능************/
    override fun addToCenter(siName: String, gunguName: String) {
        val findGunGu: GunGu = gunguRepo.findBySiNameAndGunguName(siName, gunguName);
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter")
        urlBuilder.append(
            "?" + URLEncoder.encode(
                "serviceKey",
                "UTF-8"
            ) + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D"
        )
        urlBuilder.append(
            "&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(
                findGunGu.siCode,
                "UTF-8"
            )
        ); /*페이지 번호*/
        urlBuilder.append(
            "&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(
                findGunGu.gunguCode,
                "UTF-8"
            )
        ); /*페이지 번호*/
        urlBuilder.append(
            "&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode(
                "json",
                "UTF-8"
            )
        ); /*xml(기본값) 또는 json*/
        val url = URL(urlBuilder.toString())
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        val input = conn.getInputStream()
        val isr = InputStreamReader(input)
        // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
        val br = BufferedReader(isr)
        var str: String? = null
        val buf = StringBuffer()

        do {
            str = br.readLine()

            if (str != null) {
                buf.append(str)
            }
        } while (str != null)
        val root = JSONObject(buf.toString())
        val response = root.getJSONObject("response").getJSONObject("body").getJSONObject("items")
        val item = response.getJSONArray("item") // 객체 안에 있는 item이라는 이름의 리스트를 가져옴
        for (i in 0 until item.length()) {
            val center = Center();
            var jObject = item.getJSONObject(i)
            center.centerCode = jObject.get("careRegNo").toString()
            center.centerName = jObject.get("careNm").toString()
            center.siName = findGunGu.siName
            center.gunguName = findGunGu.gunguName
            centerRepo.save(center)

        }
    }

}