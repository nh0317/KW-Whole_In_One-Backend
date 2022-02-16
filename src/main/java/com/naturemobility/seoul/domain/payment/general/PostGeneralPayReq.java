package com.naturemobility.seoul.domain.payment.general;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostGeneralPayReq {
    private Long reservationIdx;
    private Long storeIdx;
    private Long couponIdx;
    private String impUid;
    private String merchantUid;
    private String payMethod;
    private Integer amount;
    private Integer point;
}
