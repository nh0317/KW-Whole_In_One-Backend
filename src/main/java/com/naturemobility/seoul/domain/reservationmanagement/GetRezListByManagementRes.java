package com.naturemobility.seoul.domain.reservationmanagement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetRezListByManagementRes {
    final Long reservationIdx;
    final Long roomIdx;
    final String reservationTime;
    final String endTime;
    final Integer selectedHall;
    final Integer personCount;
    final Integer numberOfGame;
    final Integer useTime;
    final Integer reservePrice;
    final String request;
}
