package com.naturemobility.seoul.domain.reservations;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GetRezTimes {
    private String time;
    private Integer period;

    public boolean isValid(int playTime){
        if(period == null)
            return true;
        else if (period >= playTime)
            return true;
        else return false;
    }
}
