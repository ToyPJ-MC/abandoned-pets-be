package Winter.pets.domain.kind

import lombok.Data
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
    var noticeSdt:String?=null
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
    var noticeEdt:String?=null
    var neuterYn:String?=null
    var specialMark:String?=null
    var colorCd:String?=null
    var happenDt:String?=null
    var age:String?=null

}