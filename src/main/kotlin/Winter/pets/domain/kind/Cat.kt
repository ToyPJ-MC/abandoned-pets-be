package Winter.pets.domain.kind

import lombok.Data
import javax.persistence.*

@Entity
@Data
class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cat_id")
    val id:Long?=null
    @Column(name="cat_code")
    var catCode:String="422400"
    @Column(name="cat_name")
    var catName:String?=null
    @Column(name="kind_code")
    var kindCode:String?=null
}