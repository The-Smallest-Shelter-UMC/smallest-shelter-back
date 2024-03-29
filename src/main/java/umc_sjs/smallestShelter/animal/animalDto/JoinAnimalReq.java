package umc_sjs.smallestShelter.animal.animalDto;

import lombok.Getter;
import lombok.Setter;
import umc_sjs.smallestShelter.domain.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JoinAnimalReq {

    private Long userIdx;
    private String name;
    private Integer year;
    private Integer month;
    private boolean isGuessed;
    private Gender gender;
    private Species species;
    private String mainImgUrl;
    private Boolean isAdopted;
    private Status socialization;
    private Status separation;
    private Status toilet;
    private Status bark;
    private Status bite;
    private List<String> illness = new ArrayList<>();

    public boolean getIsGuessed() {
        return isGuessed;
    }
}
