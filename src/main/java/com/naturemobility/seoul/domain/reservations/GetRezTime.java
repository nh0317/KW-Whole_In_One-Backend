package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetRezTime {
    final String reservationTime; //yyyy.mm.dd 오전 hh:mm
    final String endTime;
}
