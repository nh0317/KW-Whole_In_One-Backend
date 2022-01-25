package com.naturemobility.seoul.domain.userPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPayReq {
    private Long userPaymentIdx;
    private Long storeIdx;
    private Integer amount;
}
