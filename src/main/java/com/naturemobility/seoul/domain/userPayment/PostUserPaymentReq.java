package com.naturemobility.seoul.domain.userPayment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUserPaymentReq {
    private String billingKey;
    private String cardNumber;
    private Integer cardType;
    private Integer cardCode;
}
