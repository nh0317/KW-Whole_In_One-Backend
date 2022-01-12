package com.naturemobility.seoul.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetRezListRes {
    final String reservationTime;
    final Integer selectedHall;
    final Integer personCount;
    final Integer numberOfGame;
    final Integer userTime;
}
