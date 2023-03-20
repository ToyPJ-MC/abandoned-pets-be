package Winter.pets.domain.kind

import Winter.pets.domain.jwt.domain.Member
import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Data
class SelectPets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pets_id")
    val id:Long?=null
    var sexCd:String?=null
    var kindCd:String?=null
    var noticeNo:String?=null
    var processState:String?=null
    var careAddr:String?=null
    lateinit var noticeSdt:Date
    var weight:String?=null
    var chargeNm:String?=null
    var desertionNo:String?=null
    var careNm:String?=null
    var careTel:String?=null
    var happenPlace:String?=null
    var officetel:String?=null
    var orgNm:String?=null
    var filename:String?=null
    var popfile:String?=null
    lateinit var noticeEdt:Date
    var neuterYn:String?=null
    var specialMark:String?=null
    var colorCd:String?=null
    lateinit var happenDt: Date
    var age:String?=null
    @CreatedDate
    @Column(updatable = false)
    var createAt=LocalDateTime.now()


}