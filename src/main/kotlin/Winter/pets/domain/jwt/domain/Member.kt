package Winter.pets.domain.jwt.domain

import Winter.pets.domain.kind.SelectPets
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Member {
    @Id
    @Column(name="member_id")
    var id:Long?=null
    @Column(name="memeber_name")
    var name:String?=null
    @Column(name="member_gender")
    var gender:String?=null
    @Column(name="member_email")
    var email:String?=null
    @OneToMany(mappedBy = "member", cascade = arrayOf(CascadeType.ALL))
    var list:MutableList<SelectPets> = ArrayList<SelectPets>()
}