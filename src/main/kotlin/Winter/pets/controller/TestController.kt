package Winter.pets.controller

import Winter.pets.service.PetService
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@RestController
@RequestMapping("/api")
class TestController {

    @Autowired
    lateinit var petService: PetService
    constructor(petService: PetService){{
        this.petService = petService
    }

    }
   /* @Operation(summary = "시 db에 저장")
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
}