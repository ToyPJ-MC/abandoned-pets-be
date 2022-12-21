package Winter.pets.domain.Country

import lombok.AllArgsConstructor
import lombok.Data
import org.hibernate.annotations.Table
import javax.persistence.*

@Entity
@Data
class GunGu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gu_id")
    val id:Long?=null;
    @Column(name = "gu_code")
    var gungu_code:String?=null;
    @Column(name = "gu_name")
    var gungu_name:String?=null;
    @Column(name="si_code")
    var si_code:String?=null;
}