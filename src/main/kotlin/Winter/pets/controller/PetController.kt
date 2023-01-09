package Winter.pets.controller

import Winter.pets.domain.kind.AddPets
import Winter.pets.domain.kind.Cat
import Winter.pets.domain.kind.Dog
import Winter.pets.domain.kind.SelectPets
import Winter.pets.repository.CatRepository
import Winter.pets.repository.DogRepository
import Winter.pets.service.PetService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin("http://192.168.0.16:8080")
@RestController
@RequestMapping("/api")
class PetController(
    private val petService: PetService,
    private val dogRepo: DogRepository,
    private val catRepo: CatRepository
) {
    @Operation(summary = "유기동물 조회 기능")
    @PostMapping("find/abandonded")
    fun findToPet(@RequestParam("start_time")start:String, @RequestParam("end_time")end:String, @RequestParam("kind_code")kindCode:String,
                  @RequestParam("kind")kind:String, @RequestParam("si_code")si:String, @RequestParam("gungu_code")gungu:String, @RequestParam("center")center:String,
                  @RequestParam("state")state:String, @RequestParam("neuter")neuter:String): ResponseEntity<Any> {
        try{
            var list:List<SelectPets>
            if(kindCode.equals("417000")){
                var findDog: Dog = dogRepo.findByDogName(kind);
                list = petService.selectToPet(start,end,kindCode,findDog.kindCode.toString(),si,gungu,center,state,neuter);
                return ResponseEntity.ok().body(list);
            }
            else{
                var findCat: Cat = catRepo.findByCatName(kind);
                list = petService.selectToPet(start,end,kindCode,findCat.kindCode.toString(),si,gungu,center,state,neuter);
                return ResponseEntity.ok().body(list);
            }
        }catch (e:RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "유기동물 전체 조회 // DB 저장 되어있는 것만")
    @GetMapping("find/all")
    fun findAll(@RequestParam("page")page:Int, @RequestParam("size")size:Int): ResponseEntity<Any> {
        try{
            var list: List<AddPets> = petService.findToPet(page,size)
            return ResponseEntity.ok().body(list)
        }catch (e :RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary = "맥스 페이지 조회")
    @GetMapping("/find/page")
    fun findToPage(): ResponseEntity<Any> {
        var page = petService.findToMaxPage()
        return ResponseEntity.ok(page)
    }

    @Operation(summary = "유기 동물 db 저장")
    @PostMapping("/add/pets")
    fun addToPet(): ResponseEntity<String> {
        try{
            petService.addToPet()
            return ResponseEntity.ok().body("저장완료")
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }
    @Operation(summary="최근 검색 기록")
    @PostMapping("/find/search")
    fun findToSearchList(): ResponseEntity<Any> {
        try{
            var list = petService.findToSearchList()
            return ResponseEntity.ok().body(list)
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }

    @Operation(summary="db 최신화시키기 위해 저장된 pet delete 기능")
    @PostMapping("/delete")
    fun deleteToPet(): ResponseEntity<Any> {
        try{
            petService.deleteToPet()
            return ResponseEntity.ok().body("delete 완료")
        }catch (e : RuntimeException){
            return ResponseEntity.badRequest().body("잘못된 조회")
        }
    }

}