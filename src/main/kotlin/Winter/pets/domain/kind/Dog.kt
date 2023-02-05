package Winter.pets.domain.kind

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Data
class Dog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="dog_id")
    val id:Long?=null
    @Column(name="dog_code",nullable = false)
    var dogCode:String="417000"
    @Column(name="dog_name",nullable = false)
    var dogName:String?=null
    @Column(name="kind_code",nullable = false)
    var kindCode:String?=null
}