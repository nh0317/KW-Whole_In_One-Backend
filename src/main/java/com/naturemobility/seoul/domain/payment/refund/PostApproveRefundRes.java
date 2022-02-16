package com.naturemobility.seoul.domain.payment.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostApproveRefundRes {
    private final Long reservationIdx;
    private final String refundReason;
    private final int cancelAmount;
    private final int point;
    private final String refundStatus;
}
