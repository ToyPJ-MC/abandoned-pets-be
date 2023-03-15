package Winter.pets.controller

import Winter.pets.service.CenterService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@CrossOrigin("http://192.168.0.16:8080")
@RestController
@RequestMapping("/api")
class CenterController(private val centerService: CenterService) {
    @Operation(summary="시 군구에 대한 보호소 db 저장 * 조회 금지 db 중복 ")
    @GetMapping("/add/center")
    fun AddToCenter(siName:String, gunguName: String):ResponseEntity<String>{
        try{
            centerService.addToCenter(siName,gunguName)
            return ResponseEntity.ok().body(siName+" " + gunguName+" 추가되었습니다.")
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
    @Operation(summary = "시 군구에 대한 보호소 찾기")
    @PostMapping("/find/center")
    fun findToCenter(@RequestParam("si_name") siName:String, @RequestParam("gungu_name") gunguName:String): ResponseEntity<Any> {
        try{
            val list = centerService.findToCenter(siName,gunguName);
            return ResponseEntity.ok().body(list);
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}