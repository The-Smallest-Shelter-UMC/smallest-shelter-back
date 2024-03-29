package umc_sjs.smallestShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FavoriteAnimal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favoriteAnimal_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "likeUser_Idx")
    private User likeUser;

    @ManyToOne
    @JoinColumn(name = "animal_idx")
    private Animal animal;

    //연관관계 편의 메소드
    public void modifyLikeUserAndAnimal(User likeUser, Animal animal) {
        this.likeUser = likeUser;
        likeUser.getFavoriteAnimalList().add(this);
        this.animal = animal;
    }

}
