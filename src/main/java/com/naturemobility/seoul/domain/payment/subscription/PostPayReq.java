package com.naturemobility.seoul.domain.payment.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayReq {
    private Long reservationIdx;
    private Long storeIdx;
    private Long userPaymentIdx;
    private String payMethod;
    private Integer amount;
    private Integer point;
}
