package Winter.pets.controller

import Winter.pets.service.SiService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SiController(private val siService: SiService) {
    @Operation(summary = "시 db에 저장 조회 금지 db 중복됨")
    @GetMapping("/add/si")
    fun AddToSi(): ResponseEntity<String> {
        try {
            siService.addToCountry();
            return ResponseEntity.ok().body("시/광역시가 추가되었습니다.")
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }

    }
}