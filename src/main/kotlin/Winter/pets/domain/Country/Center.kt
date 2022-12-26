package Winter.pets.domain.Country

import lombok.Data
import javax.persistence.*

@Entity
@Data
class Center {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    val id:Long?=null;
    @Column(name = "center_code")
    var centerCode:String?=null;
    @Column(name = "center_name")
    var centerName:String?=null;
    @Column(name ="si_name")
    var siName:String?=null
    @Column(name ="gungu_name")
    var gunguName:String?=null


}