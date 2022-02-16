package com.naturemobility.seoul.domain.payment;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo extends DTOCommon {
    // 주문 번호
    private String merchantUid;

    private Long reservationIdx;

    private Long userPaymentIdx;

    private Long couponIdx;

    private String impUid;

    private String payMethod;

    private Long userIdx;

    private Long storeIdx;

    // 결제 금액
    private int amount;

    // 결제 이름(상품명)
    private String name;

    private Integer point;
    // 환불 여부
    private Integer refundStatus;

    private String refundReason;
    private String refundHolder;
    private String refundBank;
    private String refundAccount;
    private int cancelAmount;
    private Long modiBy;
    //수정한 사람의 권한 (MEMBER/ADMIN)
    private String modiRole;

    public PaymentInfo(String merchantUid, Long reservationIdx, Long userPaymentIdx, Long couponIdx, String impUid,
                       String payMethod, Long userIdx, Long storeIdx, int amount, String name, Integer point,
                       Long modiBy, String modiRole) {
        this.merchantUid = merchantUid;
        this.reservationIdx = reservationIdx;
        this.userPaymentIdx = userPaymentIdx;
        this.couponIdx = couponIdx;
        this.impUid = impUid;
        this.payMethod = payMethod;
        this.userIdx = userIdx;
        this.storeIdx = storeIdx;
        this.amount = amount;
        this.name = name;
        this.point = point;
        this.modiBy = modiBy;
        this.modiRole = modiRole;
        this.refundStatus=0;
    }
}