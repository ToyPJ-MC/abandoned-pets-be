package Winter.pets.service

import Winter.pets.domain.Country.Center
import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.kind.AddPets
import Winter.pets.domain.kind.SelectPets
import Winter.pets.repository.*
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

@Service
class PetServiceImpl(
    private val petRepo: PetsRepository,
    private val addPetRepo: AddToPetRepository,
    private val gunguRepo: gunguRepository,
    private val centerRepo: CenterRepository
) : PetService {

    //************************** 유기동물 select 조회 ***************************//
    override fun selectToPet(
        Start: String,
        end: String,
        kindCode: String,
        kind: String,
        si: String,
        gungu: String,
        centerCode: String,
        state: String,
        neuter: String
    ): List<SelectPets> {
        val findGungu: GunGu = gunguRepo.findBySiNameAndGunguName(si, gungu)
        val findCenter: Center = centerRepo.findByCenterNameAndGunguName(centerCode, gungu)
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic")
        urlBuilder.append(
            "?" + URLEncoder.encode(
                "serviceKey",
                "UTF-8"
            ) + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D"
        )
        urlBuilder.append(
            "&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(
                Start,
                "UTF-8"
            )
        ); /*페이지 번호*/
        urlBuilder.append(
            "&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(
                end,
                "UTF-8"
            )
        ); /*xml(기본값) 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(kindCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode(kind, "UTF-8"));
        urlBuilder.append(
            "&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(
                findGungu.siCode,
                "UTF-8"
            )
        );
        urlBuilder.append(
            "&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(
                findGungu.gunguCode,
                "UTF-8"
            )
        );
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "care_reg_no",
                "UTF-8"
            ) + "=" + URLEncoder.encode(findCenter.centerCode, "UTF-8")
        );
        urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode(neuter, "UTF-8"));
        urlBuilder.append(
            "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(
                "1",
                "UTF-8"
            )
        ); /*페이지 번호*/
        urlBuilder.append(
            "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(
                "20",
                "UTF-8"
            )
        ); /*한 페이지 결과 수(1,000 이하)*/
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

        var list = ArrayList<SelectPets>()
        for (i in 0 until item.length()) {
            var select = SelectPets()
            val jsonObject = item.getJSONObject(i)
            select.sexCd = jsonObject.getString("sexCd");select.kindCd = jsonObject.getString("kindCd");select.noticeNo = jsonObject.getString("noticeNo")
            select.processState = jsonObject.getString("processState");select.noticeSdt = jsonObject.getString("noticeSdt");select.careAddr = jsonObject.getString("careAddr")
            select.weight = jsonObject.getString("weight");select.desertionNo = jsonObject.getString("desertionNo");select.chargeNm = jsonObject.getString("chargeNm")
            select.careNm = jsonObject.getString("careNm");select.careTel = jsonObject.getString("careTel");select.happenPlace = jsonObject.getString("happenPlace")
            select.officetel = jsonObject.getString("officetel");select.orgNm = jsonObject.getString("orgNm");select.filename = jsonObject.getString("filename")
            select.popfile = jsonObject.getString("popfile");select.noticeEdt = jsonObject.getString("noticeEdt");select.neuterYn = jsonObject.getString("neuterYn")
            select.specialMark = jsonObject.getString("specialMark");select.colorCd = jsonObject.getString("colorCd");select.happenDt = jsonObject.getString("happenDt")
            select.age = jsonObject.getString("age")
            list.add(select)
            petRepo.save(select)
        }
        return list
    }

    //저장된 유기동물 전체 조회시 paging 통해 오래된 동물부터
    override fun findToPet(page: Int, size: Int): List<AddPets> {
        var request: PageRequest = PageRequest.of(page, size, Sort.by("happenDt").ascending())
        var findList: List<AddPets> = addPetRepo.findAll(request).content
        return findList
    }
    //******************************* db Max Page 조회****************************//
    override fun findToMaxPage(): String {
        var findPage = addPetRepo.findAll()
        var size = findPage.size
        return (size/6).toString()
    }

    /******************************유기동물 db 저장 ********************************/
    override fun addToPet() {
        for(i in 1 until 100){
            var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic")
            urlBuilder.append(
                "?" + URLEncoder.encode(
                    "serviceKey",
                    "UTF-8"
                ) + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D"
            )
            urlBuilder.append(
                "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(
                    i.toString(),
                    "UTF-8"
                )
            ); /*페이지 번호*/
            urlBuilder.append(
                "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(
                    "50",
                    "UTF-8"
                )
            ); /*한 페이지 결과 수(1,000 이하)*/
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
                var select = AddPets()
                val jsonObject = item.getJSONObject(i);

                var find: String? = addPetRepo.findByDesertionNo(jsonObject.getString("desertionNo"))
                if (find == null) {
                    var dtformat = SimpleDateFormat("yyyyMMdd")
                    var format = SimpleDateFormat("yyyy-MM-dd")
                    var formatDate: Date = dtformat.parse(jsonObject.getString("happenDt"))
                    var date: String = format.format(formatDate)
                    var formatNotice: Date = dtformat.parse(jsonObject.getString("noticeSdt"))
                    var dates: String = format.format(formatNotice)
                    select.sexCd = jsonObject.getString("sexCd");select.kindCd = jsonObject.getString("kindCd");
                    select.processState = jsonObject.getString("processState");select.noticeSdt = dates;select.careAddr = jsonObject.getString("careAddr")
                    select.weight = jsonObject.getString("weight");select.desertionNo = jsonObject.getString("desertionNo");
                    select.careNm = jsonObject.getString("careNm");select.careTel = jsonObject.getString("careTel");select.happenPlace = jsonObject.getString("happenPlace")
                    select.officetel = jsonObject.getString("officetel");select.popfile = jsonObject.getString("popfile");select.noticeEdt = jsonObject.getString("noticeEdt");select.neuterYn = jsonObject.getString("neuterYn")
                    select.specialMark = jsonObject.getString("specialMark");select.colorCd = jsonObject.getString("colorCd");select.happenDt = date
                    select.age = jsonObject.getString("age")
                    addPetRepo.save(select)
                }
            }
        }
    }
  //*********************** 최근 검색 조회 ***********************/
    override fun findToSearchList(): List<SelectPets> {
        var list:List<SelectPets> = petRepo.findAll()
        return list
    }

    override fun deleteToPet() {
        addPetRepo.deleteAll()
    }
}