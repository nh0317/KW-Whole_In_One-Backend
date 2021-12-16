package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetRezResByUserIdx {
    final Long reservationIdx;
    final String storeName;
    final String reservationTime;
    final Integer useTime;
    final Integer selectHall;
    final Integer personCount;
    final Boolean alreadyUsed;
}
