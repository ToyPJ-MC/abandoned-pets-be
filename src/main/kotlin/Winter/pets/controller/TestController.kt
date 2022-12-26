package Winter.pets.controller

import Winter.pets.service.PetService
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@CrossOrigin("http://192.168.0.16:8080")
@RestController
@RequestMapping("/api")
class TestController {

    @Autowired
    lateinit var petService: PetService
    constructor(petService: PetService){{
        this.petService = petService
    }

    }
    @Operation(summary = "시 db에 저장")
    @GetMapping("/add/si")
    fun AddToSi(): ResponseEntity<String>{
        try {
            val list = petService.addToCountry();
            return ResponseEntity.ok().body("시/광역시가 추가되었습니다.")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }

    @Operation(summary="시에 대한 군구 db에 저장")
    @GetMapping("/add/gungu")
    fun AddToGungu(name:String): ResponseEntity<String>{
        try{
            val list = petService.addToGu(name);
            return ResponseEntity.ok().body("군/구가 추가되었습니다.")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
   @Operation(summary="시 군구에 대한 보호소 db 저장")
   @GetMapping("/add/center")
   fun AddToCenter(siName:String, gunguName: String):ResponseEntity<String>{
       try{
           val list = petService.addToCenter(siName,gunguName)
           return ResponseEntity.ok().body("센터가 추가되었습니다.")
       }catch (e: RuntimeException){
           return ResponseEntity.badRequest().body("잘못된 조회")
       }

   }
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
}