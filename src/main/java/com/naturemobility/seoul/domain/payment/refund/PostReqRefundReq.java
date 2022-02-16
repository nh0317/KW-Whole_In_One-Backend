package com.naturemobility.seoul.domain.payment.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReqRefundReq {
    private Long reservationIdx;
    private String refundReason;
    private String refundHolder; // 가상계좌 환불시 필요
    private String refundBank; // 가상계좌 환불시 필요
    private String refundAccount; // 가상계좌 환불시 필요
}
