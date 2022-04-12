package com.naturemobility.seoul.domain.reservationmanagement;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetRezPerMonth {
    private String date;
    private int cntReservation;
}
