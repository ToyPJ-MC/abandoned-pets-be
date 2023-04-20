package Winter.pets.controller

import Winter.pets.domain.jwt.service.MemberService
import Winter.pets.domain.kind.Pet
import Winter.pets.domain.kind.PetDto
import Winter.pets.service.PetService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Api(tags = ["Pet"], description = "펫 API")
@RestController
@RequestMapping("/api")
class PetController(
    private val petService: PetService,
    private val memberService: MemberService
) {
   /* @Operation(summary = "유기동물 select 조회 기능", description = "Kind_Code = 품종 코드 입력, Kind = 품종")
    @PostMapping("select/memberid={member_id}/kind={kind}")
    fun findToPet(@RequestParam("start_time")start:String, @RequestParam("end_time")end:String, @RequestParam("kind_code")kindCode:String,
                  @PathVariable("kind")kind:String, @RequestParam("si_code")si:String, @RequestParam("gungu_code")gungu:String, @RequestParam("center")center:String,
                  @RequestParam("state")state:String, @RequestParam("neuter")neuter:String,@PathVariable("member_id")memberId:String): ResponseEntity<Any> {
        try{
            var list:List<SelectPets>
            if(kindCode.equals("417000")){
                var findDog: Dog = dogRepo.findByDogName(kind);
                list = petService.selectToPet(start,end,kindCode,findDog.kindCode.toString(),si,gungu,center,state,neuter,memberId);
                return ResponseEntity.ok().body(list);
            }
            else{ //422400 고양이
                var findCat: Cat = catRepo.findByCatName(kind);
                list = petService.selectToPet(start,end,kindCode,findCat.kindCode.toString(),si,gungu,center,state,neuter,memberId);
                return ResponseEntity.ok().body(list);
            }
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }*/
   @Operation(summary =  "유기동물 select 조회 기능", description = "Kind_Code = 품종 코드 입력, Kind = 개 : 417000, 고양이 : 422400")
   @GetMapping("/pets/select/kindcode={kind_code}")
   fun serachToPet(@RequestParam("access_token")token: String, @RequestParam("kind_cd") kindCd: String,
                   @RequestParam("care_nm") careNm: String,@RequestParam("org_nm") orgNm : String,
                   @RequestParam("neuter_yn") neuterYn : String,@PathVariable("kind_code") kindCode:String):Any{
       var dtoList = mutableListOf<PetDto>()
       var selectList: List<Pet>
       if(kindCode.equals("417000")){
           var kindName = "[개] "+ kindCd
           selectList = petService.selectToPet(token,kindName,careNm,orgNm,neuterYn) as List<Pet>
       }
       else{ //422400 고양이
           var kindName = "[고양이] "+ kindCd
           selectList = petService.selectToPet(token,kindName,careNm,orgNm,neuterYn) as List<Pet>
       }
       if(!token.equals("No")){
           var likeList = memberService.findToLikesList(token) as List<Pet>
           for (i in selectList){
               var cnt =0;
               for(j in likeList){
                   if(i.noticeNo.equals(j.noticeNo)){
                       var dto = PetDto(j.id!!, j.sexCd!!, j.kindCd!!,
                               j.noticeNo!!, j.processState!!, j.careAddr!!, j.noticeSdt!!.toString(),
                               j.weight!!, j.desertionNo!!, j.careNm!!, j.careTel!!,
                               j.happenPlace!!, j.officetel!!, j.orgNm!!,
                               j.filename!!, j.popfile!!, j.noticeEdt!!.toString(), j.neuterYn!!,
                               j.specialMark!!, j.colorCd!!, j.happenDt!!.toString(), j.age!!, true)
                       dtoList.add(dto)
                       break
                   }
                   cnt++
               }
               if(cnt==likeList.size) {
                   var dto = PetDto(i.id!!, i.sexCd!!, i.kindCd!!,
                           i.noticeNo!!, i.processState!!, i.careAddr!!, i.noticeSdt!!.toString(),
                           i.weight!!, i.desertionNo!!, i.careNm!!, i.careTel!!,
                           i.happenPlace!!, i.officetel!!, i.orgNm!!,
                           i.filename!!, i.popfile!!, i.noticeEdt!!.toString(), i.neuterYn!!,
                           i.specialMark!!, i.colorCd!!, i.happenDt!!.toString(), i.age!!, false)
                   dtoList.add(dto)
               }
           }
           return ResponseEntity.ok().body(dtoList)
       }
       else return ResponseEntity.ok().body(selectList)
   }
    @Operation(summary = "유기동물 페이징 조회 기능", description = "page = 페이지, size = 한 페이지에 보여줄 데이터 수")
    @GetMapping("pets/page={page}/size={size}")
    fun findAll(@PathVariable("page")page:Int, @PathVariable("size")size:Int,request : HttpServletRequest): ResponseEntity<Any> {

            var list: List<Pet> = petService.findToPet(page,size)
            val cookie = request.cookies?.find { it.name == "access_token" }
                    ?: return ResponseEntity.ok().body(list)
            var accessToken = cookie.value

            var likeList = memberService.findToLikesList(accessToken) as List<Pet>
            var dtoList = mutableListOf<PetDto>()
            for (i in list){
                var cnt =0;
                for(j in likeList){
                    if(i.noticeNo.equals(j.noticeNo)){
                        var dto = PetDto(j.id!!, j.sexCd!!, j.kindCd!!,
                                j.noticeNo!!, j.processState!!, j.careAddr!!, j.noticeSdt!!.toString(),
                                j.weight!!, j.desertionNo!!, j.careNm!!, j.careTel!!,
                                j.happenPlace!!, j.officetel!!, j.orgNm!!,
                                j.filename!!, j.popfile!!, j.noticeEdt!!.toString(), j.neuterYn!!,
                                j.specialMark!!, j.colorCd!!, j.happenDt!!.toString(), j.age!!, true)
                        dtoList.add(dto)
                        break
                    }
                    cnt++
                }
                if(cnt==likeList.size) {
                    var dto = PetDto(i.id!!, i.sexCd!!, i.kindCd!!,
                            i.noticeNo!!, i.processState!!, i.careAddr!!, i.noticeSdt!!.toString(),
                            i.weight!!, i.desertionNo!!, i.careNm!!, i.careTel!!,
                            i.happenPlace!!, i.officetel!!, i.orgNm!!,
                            i.filename!!, i.popfile!!, i.noticeEdt!!.toString(), i.neuterYn!!,
                            i.specialMark!!, i.colorCd!!, i.happenDt!!.toString(), i.age!!, false)
                    dtoList.add(dto)
                }
            }
            return ResponseEntity.ok().body(dtoList)
    }
    @Operation(summary = "맥스 페이지 조회")
    @GetMapping("/pets/page/all")
    fun findToPage(): ResponseEntity<Any> {
        var page = petService.findToMaxPage()
        return ResponseEntity.ok(page)
    }

    /*@Operation(summary = "유기 동물 db 저장")
    @PostMapping("/pets/add/all")
    fun addToPet(): ResponseEntity<String> {
        try{
            petService.addToPet()
            return ResponseEntity.ok().body("저장완료")
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }*/

    @Operation(summary = "총 유기동물 수 조회")
    @GetMapping("/pets/count/all")
    fun findToAllSize():ResponseEntity<String>{
        try{
            var find = petService.allToPet()
            return ResponseEntity.ok().body(find)
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}