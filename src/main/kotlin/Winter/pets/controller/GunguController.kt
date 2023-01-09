package Winter.pets.controller

import Winter.pets.service.GunguService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class GunguController(private val gunguService: GunguService) {
    @Operation(summary="시에 대한 군구 db에 저장")
    @GetMapping("/add/gungu")
    fun AddToGungu(name:String): ResponseEntity<String>{
        try{
            gunguService.addToGu(name);
            return ResponseEntity.ok().body("군/구가 추가되었습니다.")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
    @Operation(summary = "시에 대한 군구 찾기")
    @PostMapping("/find/gungu")
    fun findToGungu(@RequestParam("Si_name") name:String): ResponseEntity<Any> {
        try{
            val list = gunguService.findToGu(name);
            return ResponseEntity.ok().body(list);
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }

}