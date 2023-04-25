package Winter.pets.domain.Country

import lombok.Data
import javax.persistence.*

@Entity
@Data
class Center {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    val id:Long?=null;
    @Column(name = "center_code",nullable = false)
    var centerCode:String?=null;
    @Column(name = "center_name",nullable = false)
    var centerName:String?=null;
    @Column(name ="si_name",nullable = false)
    var siName:String?=null
    @Column(name ="gungu_name",nullable = false)
    var gunguName:String?=null


}