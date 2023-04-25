package Winter.pets.domain.jwt.domain

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.kind.Pet
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import lombok.Generated
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
@Data
class Member {
    @Id
    @Column(name="member_id", nullable = false)
    private val id =UUID.randomUUID().toString()
    @Column(name="memeber_name")
    var name:String?=null
    @Column(name="memeber_email",unique = true)
    var email:String?=null
    @Column(name="member_profile")
    var profile:String?=null
    @Column(length=1000)
    var accessToken:String?=null
    @Column(length=1000)
    var refreshToken:String?=null
    var role = "ROLE_USER"
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    var list:MutableList<Pet> = ArrayList<Pet>()
}