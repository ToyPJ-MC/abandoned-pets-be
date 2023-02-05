package Winter.pets.domain.Country

import lombok.AllArgsConstructor
import lombok.Builder
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
    @Column(name = "gu_code",nullable = false)
    var gunguCode:String?=null;
    @Column(name = "gu_name",nullable = false)
    var gunguName:String?=null;
    @Column(name="si_code",nullable = false)
    var siCode:String?=null;
    @Column(name="si_name",nullable = false)
    var siName:String?=null
}