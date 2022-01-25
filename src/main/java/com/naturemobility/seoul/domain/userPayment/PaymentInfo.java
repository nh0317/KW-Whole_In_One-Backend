package com.naturemobility.seoul.domain.userPayment;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentInfo extends DTOCommon {
    // 주문 번호
    private String merchantUid;

    private Long userIdx;

    private Long storeIdx;

    // 결제 금액
    private Integer amount;

    // 결제 이름(상품명)
    private String name;

    // 환불 여부
    private Boolean isRefund;

    private Long modiBy;
    //수정한 사람의 권한 (MEMBER/ADMIN)
    private String modiRole;

    public PaymentInfo(String merchantUid, Long userIdx, Long storeIdx, Integer amount, String name) {
        this.merchantUid = merchantUid;
        this.userIdx = userIdx;
        this.storeIdx = storeIdx;
        this.amount = amount;
        this.name = name;
        this.isRefund=false;
    }
}