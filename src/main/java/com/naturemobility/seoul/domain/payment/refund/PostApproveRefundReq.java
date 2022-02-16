package com.naturemobility.seoul.domain.payment.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostApproveRefundReq {
    private Long reservationIdx;
    private int cancelAmount;
}
