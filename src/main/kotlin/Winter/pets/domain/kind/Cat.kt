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
    @Column(name="cat_code",nullable = false)
    var catCode:String="422400"
    @Column(name="cat_name",nullable = false)
    var catName:String?=null
    @Column(name="kind_code",nullable = false)
    var kindCode:String?=null
}