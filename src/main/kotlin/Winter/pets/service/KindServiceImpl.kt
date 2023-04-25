package Winter.pets.service

import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.repository.CatRepository
import Winter.pets.repository.DogRepository
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
@Service
class KindServiceImpl(private val catRepo: CatRepository,
                      private val dogRepo : DogRepository) : KindService {
    /********개 고양이 품종에 따라 조회 서비스**********/
    override fun findToKind(kindName: String): List<String> {
        var catList = ArrayList<String>()
        var dogList = ArrayList<String>()
        if (kindName.equals("422400")) {
            var findCat: List<Cat> = catRepo.findAll();
            for (i in 0 until findCat.size) {
                catList.add(findCat.get(i).catName.toString())
            }
            return catList
        } else if (kindName.equals("417000")) {
            var findDog: List<Dog> = dogRepo.findAll()
            for (i in 0 until findDog.size) {
                dogList.add(findDog.get(i).dogName.toString())
            }
            return dogList
        }
        return emptyList();
    }

    override fun addToKind(kindName: String) {
        var urlBuilder = StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind")
        urlBuilder.append(
            "?" + URLEncoder.encode(
                "serviceKey",
                "UTF-8"
            ) + "=OmDc6%2BMXvh7HezqfzdkWRK4FVNXbPtLO57bVFEc8A8yJqRyA%2BUh2G2ecrVzzYtC43Fn41QpQwDmnJDId3xaj3w%3D%3D"
        )
        urlBuilder.append(
            "&" + URLEncoder.encode("up_kind_cd", "UTF-8") + "=" + URLEncoder.encode(
                kindName,
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
        if (kindName.equals("422400")) {
            for (i in 0 until item.length()) {
                val cat = Cat();
                var jObject = item.getJSONObject(i)
                cat.kindCode = jObject.getString("kindCd")
                cat.catName = jObject.getString("knm")
                catRepo.save(cat)
            }
        } else if (kindName.equals("417000")) {
            for (i in 0 until item.length()) {
                val dog = Dog();
                var jObject = item.getJSONObject(i)
                dog.kindCode = jObject.getString("kindCd")
                dog.dogName = jObject.getString("knm")
                dogRepo.save(dog)
            }
        }
    }
}