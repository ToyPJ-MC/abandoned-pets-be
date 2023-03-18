package Winter.pets.domain.jwt.domain

import Winter.pets.domain.kind.SelectPets
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
@Data
class Member {
    @Id
    @Column(name="member_id")
    var id:String?=null
    @Column(name="memeber_name")
    var name:String?=null
    @Column(name="member_email")
    var email:String?=null
    @Column(name="member_profile")
    var profile:String?=null
    var accessToken:String?=null
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    var list:MutableList<SelectPets> = ArrayList<SelectPets>()
}