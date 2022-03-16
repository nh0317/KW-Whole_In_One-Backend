package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PostCheckReservationReq {
    String startTime;
    Integer useTime;
    Long storeIdx;
    Long roomIdx;
}
