package Winter.pets.controller

import Winter.pets.domain.Country.GunGu
import Winter.pets.domain.Country.Si
import Winter.pets.service.PetService
import Winter.pets.service.PetServiceImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TestController {

    @Autowired
    lateinit var petService: PetService
    constructor(petService: PetService){{
        this.petService = petService
    }

    }
    @Operation(summary = "시 찾기")
    @GetMapping("/si")
    fun  ResponseBody(): List<Si> {
        val list = petService.findCountry();
        return list;
    }
    @Operation(summary = "시에 대한 군구 찾기")
    @GetMapping("/gungu")
    fun ResponseBody(@RequestParam("GunGu_name") name:String): List<String> {
        val list = petService.findGu(name);
        return list;
    }
}