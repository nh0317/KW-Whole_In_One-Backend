package com.naturemobility.seoul.domain.userPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserPaymentRes {
    private final Long userPaymentIdx;
    private String cardNumber;
    private String cardType;
    private String cardCode;

    public PostUserPaymentRes(Long userPaymentIdx, String cardNumber, Integer cardType, String cardCode) {
        this.userPaymentIdx=userPaymentIdx;
        this.cardNumber=cardNumber;
        this.cardCode=cardCode;
        if(cardType.equals(0))
            this.cardType="신용카드";
        else if (cardType.equals(1))
            this.cardType="체크카드";

    }
}
