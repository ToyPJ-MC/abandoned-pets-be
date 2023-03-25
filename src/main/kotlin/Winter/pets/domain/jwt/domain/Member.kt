package Winter.pets.domain.jwt.domain

import Winter.pets.domain.entity.Likes
import Winter.pets.domain.kind.Pet
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
@Data
class Member {
    @Id
    @Column(name="member_id")
    @ApiModelProperty(value = "멤버 고유 ID")
    var id:String?=null
    @Column(name="memeber_name")
    var name:String?=null
    @Column(name="member_email")
    var email:String?=null
    @Column(name="member_profile")
    var profile:String?=null
    var accessToken:String?=null
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var list:MutableList<Pet> = ArrayList<Pet>()
}