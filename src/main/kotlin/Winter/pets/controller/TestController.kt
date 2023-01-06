package Winter.pets.controller

import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.domain.kind.SelectPets
import Winter.pets.repository.CatRepository
import Winter.pets.repository.DogRepository
import Winter.pets.service.PetService

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("http://192.168.0.16:8080")
@RestController
@RequestMapping("/api")
class TestController {

    @Autowired
    lateinit var petService: PetService
    @Autowired
    lateinit var dogRepo: DogRepository
    @Autowired
    lateinit var catRepo: CatRepository
    constructor(petService: PetService,catRepo:CatRepository,dogRepo:DogRepository){{
        this.petService = petService
        this.catRepo = catRepo
        this.dogRepo = dogRepo
    }

    }
    /*@Operation(summary = "시 db에 저장")
    @GetMapping("/add/si")
    fun AddToSi(): ResponseEntity<String>{
        try {
            petService.addToCountry();
            return ResponseEntity.ok().body("시/광역시가 추가되었습니다.")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }

    @Operation(summary="시에 대한 군구 db에 저장")
    @GetMapping("/add/gungu")
    fun AddToGungu(name:String): ResponseEntity<String>{
        try{
            petService.addToGu(name);
            return ResponseEntity.ok().body("군/구가 추가되었습니다.")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
   @Operation(summary="시 군구에 대한 보호소 db 저장")
   @GetMapping("/add/center")
   fun AddToCenter(siName:String, gunguName: String):ResponseEntity<String>{
       try{
           petService.addToCenter(siName,gunguName)
           return ResponseEntity.ok().body("센터가 추가되었습니다.")
       }catch (e: RuntimeException){
           return ResponseEntity.badRequest().body("잘못된 조회")
       }

   }*/
    @Operation(summary = "시에 대한 군구 찾기")
    @PostMapping("/find/gungu")
    fun findToGungu(@RequestParam("Si_name") name:String): ResponseEntity<Any> {
        try{
            val list = petService.findToGu(name);
            return ResponseEntity.ok().body(list);
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
   @Operation(summary = "시 군구에 대한 보호소 찾기")
    @PostMapping("/find/center")
    fun findToCenter(@RequestParam("si_name") siName:String,@RequestParam("gungu_name") gunguName:String): ResponseEntity<Any> {
        try{
            val list = petService.findToCenter(siName,gunguName);
            return ResponseEntity.ok().body(list);
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    /*@Operation(summary="품종 db 저장")
    @GetMapping("/add/kind")
    fun AddToKind(kindName: String):ResponseEntity<String>{
        try{

            if(kindName.equals("417000")) {
                val dog=Dog()
                petService.addToKind(dog.dogCode)
                return ResponseEntity.ok().body("개가 추가되었습니다.")
            }
            else{
                val cat= Cat()
                petService.addToKind(cat.catCode)
                return ResponseEntity.ok().body("고양아가 추가되었습니다.")
            }

        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }*/
    @Operation(summary = "품종 찾기")
    @PostMapping("/find/kind")
    fun findToKind(@RequestParam("kind_name") kindName:String): ResponseEntity<Any> {
        try{
            var list:List<String>;
            if(kindName.equals("417000")) {
                val dog=Dog()
                list = petService.findToKind(dog.dogCode)
                return ResponseEntity.ok().body(list);
            }
            else{
                val cat= Cat()
                list = petService.findToKind(cat.catCode)
                return ResponseEntity.ok().body(list);
            }
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "유기동물 서버 저장 및 조회 기능")
    @PostMapping("find/abandonded")
    fun findToPet(@RequestParam("start_time")start:String,@RequestParam("end_time")end:String,@RequestParam("kind_code")kindCode:String,
    @RequestParam("kind")kind:String,@RequestParam("si_code")si:String,@RequestParam("gungu_code")gungu:String,@RequestParam("center")center:String,
    @RequestParam("state")state:String,@RequestParam("neuter")neuter:String):ResponseEntity<Any>{
        try{
            var list:List<SelectPets>
            if(kindCode.equals("417000")){
                var findDog:Dog = dogRepo.findByDogName(kind);
                list = petService.addToPet(start,end,kindCode,findDog.kindCode.toString(),si,gungu,center,state,neuter);
                return ResponseEntity.ok().body(list);
            }
            else{
                var findCat:Cat = catRepo.findByCatName(kind);
                list = petService.addToPet(start,end,kindCode,findCat.kindCode.toString(),si,gungu,center,state,neuter);
                return ResponseEntity.ok().body(list);
            }
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "유기동물 전체 조회 // DB 저장 되어있는 것만")
    @GetMapping("find/all")
    fun findAll(@RequestParam("page")page:Int,@RequestParam("size")size:Int):ResponseEntity<Any>{
        try{
            var list: List<SelectPets> = petService.findToPet(page,size)
            return ResponseEntity.ok().body(list)
        }catch (e :RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "맥스 페이지 조회")
    @GetMapping("/find/page")
    fun findToPage():ResponseEntity<Any>{
        var page = petService.findToMaxPage()
        return ResponseEntity.ok(page)
    }
}