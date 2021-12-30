package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class GetRezRes {
    final Long reservationIdx;
    final String storeName;
    final String reservationTIme; //yyyy.mm.dd 오전 hh:mm
    final String paymentTime; // yyyy.mm.dd
    final Integer useTime;
    final Integer selectedHall;
    final Integer personCount;
    final Boolean alreadyUsed;
    final Integer reservePrice;
    final Integer discountPrice;
    final Integer payPrice;
}
