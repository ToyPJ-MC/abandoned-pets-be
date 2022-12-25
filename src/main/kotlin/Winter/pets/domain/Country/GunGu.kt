package Winter.pets.domain.Country

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import org.hibernate.annotations.Table
import javax.persistence.*

@Entity
@Data
@Builder
class GunGu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gu_id")
    val id:Long?=null;
    @Column(name = "gu_code")
    var gunguCode:String?=null;
    @Column(name = "gu_name")
    var gunguName:String?=null;
    @Column(name="si_code")
    var siCode:String?=null;
    @Column(name="si_name")
    var siName:String?=null
}