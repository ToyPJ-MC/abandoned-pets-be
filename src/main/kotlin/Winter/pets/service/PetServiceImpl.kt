package Winter.pets.service

import Winter.pets.domain.Country.Center
import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.domain.kind.SelectPets
import Winter.pets.repository.*
import lombok.extern.slf4j.Slf4j
import org.json.JSONArray
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import java.net.HttpURLConnection
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

@Service @Slf4j
class PetServiceImpl : PetService {
    @Autowired
    lateinit var countryRepository : CountryRepository;
    @Autowired
    lateinit var gunguRepo: gunguRepository;
    @Autowired
    lateinit var centerRepo: CenterRepository;
    @Autowired
    lateinit var catRepo : CatRepository;
    @Autowired
    lateinit var dogRepo : DogRepository;
    @Autowired
    lateinit var petRepo : PetsRepository;
    constructor(gunguRepo: gunguRepository, countryRepository: CountryRepository,
                centerRepo: CenterRepository,dogRepo:DogRepository,catRepo:CatRepository,petRepo : PetsRepository) {
        this.countryRepository = countryRepository
        this.gunguRepo = gunguRepo
        this.centerRepo = centerRepo
        this.catRepo = catRepo
        this.dogRepo = dogRepo
        this.petRepo = petRepo
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

    override fun addToCenter(siName:String,gunguName:String) {
        val findGunGu: GunGu = gunguRepo.findBySiNameAndGunguName(siName,gunguName);
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter")
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D")
        urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(findGunGu.siCode, "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("org_cd","UTF-8") + "=" + URLEncoder.encode(findGunGu.gunguCode, "UTF-8")); /*페이지 번호*/
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
            val center= Center();
            var jObject = item.getJSONObject(i)
            center.centerCode = jObject.get("careRegNo").toString()
            center.centerName = jObject.get("careNm").toString()
            center.siName = findGunGu.siName
            center.gunguName = findGunGu.gunguName
            centerRepo.save(center)

        }
    }
    //시 군구에 대한 보호소 조회 기능
    override fun findToCenter(siName:String,gunguName:String): List<String> {
        val findCenter: List<Center> = centerRepo.findBySiNameAndGunguName(siName,gunguName);
        val list = ArrayList<String>()
        for(i in 0 until findCenter.size){
            list.add(findCenter.get(i).centerName.toString())
        }
        return list
    }

    override fun addToKind(kindName: String) {
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind")
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D")
        urlBuilder.append("&" + URLEncoder.encode("up_kind_cd","UTF-8") + "=" + URLEncoder.encode(kindName, "UTF-8")); /*페이지 번호*/
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
        if(kindName.equals("422400")){
            for(i in 0 until item.length()){
                val cat=Cat();
                var jObject = item.getJSONObject(i)
                cat.kindCode = jObject.getString("kindCd")
                cat.catName = jObject.getString("knm")
                catRepo.save(cat)
            }
        }
        else if(kindName.equals("417000")){
            for(i in 0 until item.length()){
                val dog=Dog();
                var jObject = item.getJSONObject(i)
                dog.kindCode = jObject.getString("kindCd")
                dog.dogName = jObject.getString("knm")
                dogRepo.save(dog)
            }
        }
    }

    override fun findToKind(kindName: String): List<String> {
        var catList=ArrayList<String>()
        var dogList=ArrayList<String>()
        if(kindName.equals("422400")){
            var findCat:List<Cat> = catRepo.findAll();
            for (i in 0 until findCat.size){
                catList.add(findCat.get(i).catName.toString())
            }
            return catList
        }
        else if(kindName.equals("417000")){
            var findDog:List<Dog> = dogRepo.findAll()
            for(i in 0 until findDog.size){
                dogList.add(findDog.get(i).dogName.toString())
            }
            return dogList
        }
        return emptyList();
    }
    //저장된 유기동물 전체 조회시 paging 통해 오래된 동물부터
    override fun findToPet(page:Int,size:Int): List<SelectPets> {
        var request :PageRequest = PageRequest.of(page,size, Sort.by("happenDt").ascending())
        var findList : List<SelectPets> = petRepo.findAll(request).content
        return findList
    }
    // 유기동물 조회 및 db 저장 기능 및 날짜 파싱
    override fun addToPet(
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
        val findGungu:GunGu = gunguRepo.findBySiNameAndGunguName(si,gungu)
        val findCenter:Center = centerRepo.findByCenterNameAndGunguName(centerCode,gungu)
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic")
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D")
        urlBuilder.append("&" + URLEncoder.encode("bgnde","UTF-8") + "=" + URLEncoder.encode(Start, "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("endde","UTF-8") + "=" + URLEncoder.encode(end, "UTF-8")); /*xml(기본값) 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("upkind","UTF-8") + "=" + URLEncoder.encode(kindCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("kind","UTF-8") + "=" + URLEncoder.encode(kind, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(findGungu.siCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd","UTF-8") + "=" + URLEncoder.encode(findGungu.gunguCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("care_reg_no","UTF-8") + "=" + URLEncoder.encode(findCenter.centerCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(state, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("neuter_yn","UTF-8") + "=" + URLEncoder.encode(neuter, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수(1,000 이하)*/
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
            var select = SelectPets()
            val jsonObject = item.getJSONObject(i);

            var find: String? = petRepo.findByDesertionNo(jsonObject.getString("desertionNo"))
            if(find==null){
                var dtformat = SimpleDateFormat("yyyyMMdd")
                var format = SimpleDateFormat("yyyy-MM-dd")
                var formatDate : Date =dtformat.parse(jsonObject.getString("happenDt"))
                var date:String = format.format(formatDate)
                var formatNotice : Date = dtformat.parse(jsonObject.getString("noticeSdt"))
                var dates:String = format.format(formatNotice)
                select.sexCd = jsonObject.getString("sexCd");select.kindCd=jsonObject.getString("kindCd");select.noticeNo=jsonObject.getString("noticeNo")
                select.processState = jsonObject.getString("processState");select.noticeSdt =dates;select.careAddr = jsonObject.getString("careAddr")
                select.weight = jsonObject.getString("weight");select.desertionNo = jsonObject.getString("desertionNo");select.chargeNm = jsonObject.getString("chargeNm")
                select.careNm = jsonObject.getString("careNm");select.careTel = jsonObject.getString("careTel");select.happenPlace = jsonObject.getString("happenPlace")
                select.officetel = jsonObject.getString("officetel");select.orgNm = jsonObject.getString("orgNm");select.filename = jsonObject.getString("filename")
                select.popfile = jsonObject.getString("popfile");select.noticeEdt = jsonObject.getString("noticeEdt");select.neuterYn = jsonObject.getString("neuterYn")
                select.specialMark = jsonObject.getString("specialMark");select.colorCd = jsonObject.getString("colorCd");select.happenDt = date
                select.age = jsonObject.getString("age")
                petRepo.save(select)
            }
        }
        var list = ArrayList<SelectPets>()
        for(i in 0 until item.length()){
            var select = SelectPets()
            val jsonObject = item.getJSONObject(i)
            select.sexCd = jsonObject.getString("sexCd");select.kindCd=jsonObject.getString("kindCd");select.noticeNo=jsonObject.getString("noticeNo")
            select.processState = jsonObject.getString("processState");select.noticeSdt =jsonObject.getString("noticeSdt");select.careAddr = jsonObject.getString("careAddr")
            select.weight = jsonObject.getString("weight");select.desertionNo = jsonObject.getString("desertionNo");select.chargeNm = jsonObject.getString("chargeNm")
            select.careNm = jsonObject.getString("careNm");select.careTel = jsonObject.getString("careTel");select.happenPlace = jsonObject.getString("happenPlace")
            select.officetel = jsonObject.getString("officetel");select.orgNm = jsonObject.getString("orgNm");select.filename = jsonObject.getString("filename")
            select.popfile = jsonObject.getString("popfile");select.noticeEdt = jsonObject.getString("noticeEdt");select.neuterYn = jsonObject.getString("neuterYn")
            select.specialMark = jsonObject.getString("specialMark");select.colorCd = jsonObject.getString("colorCd");select.happenDt = jsonObject.getString("happenDt")
            select.age = jsonObject.getString("age")
            list.add(select)
        }
        return list
    }

}
