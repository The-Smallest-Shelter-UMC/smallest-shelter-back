package umc_sjs.smallestShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Animal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_idx")
    private Long idx;

    private String name;

    @Embedded
    private Age age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Species species;

    private String mainImgUrl;


    private Boolean isAdopted = false;

    @Enumerated(EnumType.STRING)
    private Status socialization;

    @Enumerated(EnumType.STRING)
    private Status separation;

    @Enumerated(EnumType.STRING)
    private Status toilet;

    @Enumerated(EnumType.STRING)
    private Status bark;

    @Enumerated(EnumType.STRING)
    private Status bite;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uploadUser_idx")
    private User uploadUser;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<Illness> illnessList = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.REMOVE)
    private List<FavoriteAnimal> favoriteAnimalList = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createDate;

    //연관관계 편의 메소드
    public void modifyUploadUser(User uploadUser) {
        this.setUploadUser(uploadUser);
        uploadUser.getUploadAnimalList().add(this);
    }

}
