package Winter.pets.domain.Country

import lombok.Builder
import lombok.Data

import org.hibernate.annotations.Table
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
@Entity
@Data
class Si {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "si_id")
    val id:Long?=null;
    @Column(name = "code",nullable = false)
    var code:String?=null;
    @Column(name = "name",nullable = false)
    var name:String?=null;
}