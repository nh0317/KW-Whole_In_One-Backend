package com.naturemobility.seoul.domain.payment;

import com.naturemobility.seoul.domain.DTOCommon;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    //수정한 사람의 권한 (ROLE_MEMBER/ROLE_ADMIN)
    private String modiRole;

    public PaymentInfo(String merchantUid, Long userPaymentIdx, Long couponIdx, String impUid,
                       String payMethod, Long userIdx, Long storeIdx, int amount, String name, Integer point,
                       Long modiBy, String modiRole) {
        this.merchantUid = merchantUid;
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

    public PaymentInfo(Long couponIdx, Long userIdx, Integer point, String refundReason, int cancelAmount, Long modiBy, String modiRole) {
        this.couponIdx = couponIdx;
        this.userIdx = userIdx;
        this.point = point;
        this.refundReason = refundReason;
        this.cancelAmount = cancelAmount;
        this.modiBy = modiBy;
        this.modiRole = modiRole;
    }

    public PaymentInfo(String merchantUid, PostGeneralPayReq postGeneralPayReq, Long userIdx, String modiRole,String name) {
        this.merchantUid = merchantUid;
        this.userPaymentIdx = null;
        this.couponIdx = postGeneralPayReq.getCouponIdx();
        this.impUid = postGeneralPayReq.getImpUid();
        this.payMethod = postGeneralPayReq.getPayMethod();
        this.userIdx = userIdx;
        this.storeIdx = postGeneralPayReq.getStoreIdx();
        this.amount = 0;
        this.name = name;
        this.point = postGeneralPayReq.getPoint();
        this.modiBy = userIdx;
        this.modiRole = modiRole;
        this.refundStatus=0;

    }
}