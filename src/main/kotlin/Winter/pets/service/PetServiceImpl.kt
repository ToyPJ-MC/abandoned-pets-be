package Winter.pets.service

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import Winter.pets.repository.CountryRepository
import Winter.pets.repository.gunguRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import java.net.HttpURLConnection
import kotlin.text.StringBuilder

@Service @Slf4j
class PetServiceImpl : PetService {
    @Autowired
    lateinit var countryRepository : CountryRepository;
    @Autowired
    lateinit var gunguRepo: gunguRepository;

    constructor(gunguRepo: gunguRepository, countryRepository: CountryRepository) {
        this.countryRepository = countryRepository
        this.gunguRepo = gunguRepo
    }

    //시 db에 저장
    override fun addToCountry() {
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("17", "UTF-8")); /*한 페이지 결과 수(1,000 이하)*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
        val url = URL(urlBuilder.toString());
        val conn:HttpURLConnection = url.openConnection() as HttpURLConnection;
        val input = conn.getInputStream()
        val isr = InputStreamReader(input)
        // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
        val br = BufferedReader(isr)
        var str: String? = null
        val buf = StringBuffer()

        do{
            str = br.readLine()

            if(str!=null){
                buf.append(str)
            }
        }while (str!=null)
        val root = JSONObject(buf.toString())
        val response = root.getJSONObject("response").getJSONObject("body").getJSONObject("items")
        val item = response.getJSONArray("item") // 객체 안에 있는 item이라는 이름의 리스트를 가져옴
        var list = ArrayList<Si>()
        for(i in 0 until item.length()){
            var si=Si();
            val jObject = item.getJSONObject(i)


            si.name = jObject.get("orgdownNm").toString()
            si.code =jObject.get("orgCd").toString()
            list.add(si);
            countryRepository.save(si)
            }
        }
    //시에 대한 군/구 저장
    override fun addToGu(name: String) {
        val findCode: Si = countryRepository.findByName(name)
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sigungu")
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(findCode.code, "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
        val url = URL(urlBuilder.toString())
        val conn:HttpURLConnection = url.openConnection() as HttpURLConnection
        val input = conn.getInputStream()
        val isr = InputStreamReader(input)
        // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
        val br = BufferedReader(isr)
        var str: String? = null
        val buf = StringBuffer()

        do{
            str = br.readLine()

            if(str!=null){
                buf.append(str)
            }
        }while (str!=null)
        val root = JSONObject(buf.toString())
        val response = root.getJSONObject("response").getJSONObject("body").getJSONObject("items")
        val item = response.getJSONArray("item") // 객체 안에 있는 item이라는 이름의 리스트를 가져옴
        for(i in 0 until item.length()){
            val gungu=GunGu()
            val jObject = item.getJSONObject(i)
            gungu.siCode = jObject.getString("uprCd")
            gungu.gunguCode = jObject.getString("orgCd")
            gungu.gunguName = jObject.getString("orgdownNm")
            gungu.siName = findCode.name;
            gunguRepo.save(gungu)
        }
    }
    //군/구 찾아서 조회
    override fun findToGu(name: String): List<String> {
        val findGunGu: List<GunGu> = gunguRepo.findBysiName(name);
        val list = ArrayList<String>()
        for(i in 0 until findGunGu.size){
            list.add(findGunGu.get(i).gunguName.toString())
        }
        return list
    }

}
