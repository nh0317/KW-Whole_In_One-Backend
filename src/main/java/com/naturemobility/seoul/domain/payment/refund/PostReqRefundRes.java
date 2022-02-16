package com.naturemobility.seoul.domain.payment.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostReqRefundRes {
    private final Long reservationIdx;
    private final String refundReason;
    private final String refundHolder; // 가상계좌 환불시 필요
    private final String refundBank; // 가상계좌 환불시 필요
    private final String refundAccount; // 가상계좌 환불시 필요
    private final String refundStatus;
}
