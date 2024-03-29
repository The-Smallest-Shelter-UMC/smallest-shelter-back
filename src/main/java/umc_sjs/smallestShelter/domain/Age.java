package umc_sjs.smallestShelter.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Age {

    private Integer year;
    private Integer month;
    private boolean isGuessed;

    public Age(Integer year, Integer month, boolean isGuessed) {
        this.year = year;
        this.month = month;
        this.isGuessed = isGuessed;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public boolean getIsGuessed() {
        return isGuessed;
    }

    public void setIsGuessed(boolean guessed) {
        isGuessed = guessed;
    }
}
