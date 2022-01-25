package com.naturemobility.seoul.domain.userPayment;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 카드 결제 정보 저장
public class UserPaymentInfo extends DTOCommon {

    private Long userPaymentIdx;

    // 유저 인덱스
    private Long userIdx;

    // 카드사
    private String billingKey;

    // 카드정보
    private String cardNumber;

    private Integer cardType;
    private String cardCode;


    public UserPaymentInfo(Long userIdx, String billingKey, String cardNumber, Integer cardType, String cardCode) {
        this.userIdx = userIdx;
        this.billingKey = billingKey;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardCode = cardCode;
    }
}
