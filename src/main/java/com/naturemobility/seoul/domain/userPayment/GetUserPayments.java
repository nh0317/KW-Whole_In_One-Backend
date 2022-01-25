package com.naturemobility.seoul.domain.userPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserPayments {
    private final Long userPaymentIdx;
    private final String cardNumber;
    private final String cardCode;
    private  String cardType;
    private final Boolean isMain;
}
