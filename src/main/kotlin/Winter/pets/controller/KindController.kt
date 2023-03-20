package Winter.pets.controller

import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.service.KindService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@Api(tags = ["Kind"], description = "품종 API")
@RequestMapping("api")
@RestController
class KindController(private val kindService: KindService) {
    @Operation(summary="품종 db 저장", description = "강아지 : 417000, 고양이 : 422400")
    @GetMapping("/kind/add/kindcode={kind_code}")
    fun AddToKind(@PathVariable("kind_code")kindcode: String):ResponseEntity<String>{
        try{

            if(kindcode.equals("417000")) {
                val dog=Dog()
                kindService.addToKind(dog.dogCode)
                return ResponseEntity.ok().body("개가 추가되었습니다.")
            }
            else{ //422400 고양이
                val cat= Cat()
                kindService.addToKind(cat.catCode)
                return ResponseEntity.ok().body("고양이가 추가되었습니다.")
            }

        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
    @Operation(summary = "품종 찾기", description = "강아지 : 417000, 고양이 : 422400")
    @PostMapping("/kind/find/kindcode={kind_code}")
    fun findToKind(@PathVariable("kind_code")kindcode: String): ResponseEntity<Any> {
        try{
            var list:List<String>;
            if(kindcode.equals("417000")) {
                val dog= Dog()
                list = kindService.findToKind(dog.dogCode)
                return ResponseEntity.ok().body(list);
            }
            else{ //422400 고양이
                val cat= Cat()
                list = kindService.findToKind(cat.catCode)
                return ResponseEntity.ok().body(list);
            }
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}