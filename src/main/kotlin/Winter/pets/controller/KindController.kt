package Winter.pets.controller

import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.service.KindService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("api")
@RestController
class KindController(private val kindService: KindService) {
    @Operation(summary="품종 db 저장  조회 금지 db 중복됨")
    @GetMapping("/add/kind")
    fun AddToKind(kindName: String):ResponseEntity<String>{
        try{

            if(kindName.equals("417000")) {
                val dog=Dog()
                kindService.addToKind(dog.dogCode)
                return ResponseEntity.ok().body("개가 추가되었습니다.")
            }
            else{
                val cat= Cat()
                kindService.addToKind(cat.catCode)
                return ResponseEntity.ok().body("고양이가 추가되었습니다.")
            }

        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
    @Operation(summary = "품종 찾기")
    @PostMapping("/find/kind")
    fun findToKind(@RequestParam("kind_name") kindName:String): ResponseEntity<Any> {
        try{
            var list:List<String>;
            if(kindName.equals("417000")) {
                val dog= Dog()
                list = kindService.findToKind(dog.dogCode)
                return ResponseEntity.ok().body(list);
            }
            else{
                val cat= Cat()
                list = kindService.findToKind(cat.catCode)
                return ResponseEntity.ok().body(list);
            }
        }catch (e: RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
}