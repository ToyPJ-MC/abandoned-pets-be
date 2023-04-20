package Winter.pets.domain.kind

import java.util.*

data class PetDto(
        var id:Long,
        var sexCd:String,
        var kindCd:String,
        var noticeNo:String,
        var processState:String,
        var careAddr:String,
        var noticeSdt: String,
        var weight:String,
        var desertionNo:String,
        var careNm:String,
        var careTel:String,
        var happenPlace:String,
        var officetel: String,
        var orgNm:String,
        var filename:String,
        var popfile:String,
        var noticeEdt: String,
        var neuterYn:String,
        var specialMark:String,
        var colorCd:String,
        var happenDt: String,
        var age: String,
        var like:Boolean
)