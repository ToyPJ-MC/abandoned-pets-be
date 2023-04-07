package Winter.pets.domain.entity

import Winter.pets.domain.jwt.domain.Member
import Winter.pets.domain.kind.Pet
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.Date
import javax.persistence.*

@Entity
class Likes {
    @Id
    @Column(name="likes_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?=null
    @CreatedDate
    @Column(updatable = false)
    var createAt= LocalDateTime.now()
    @ManyToOne
    @JoinColumn(name="member_email")
    lateinit var member:Member

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var list:MutableList<Pet> = ArrayList<Pet>()
}