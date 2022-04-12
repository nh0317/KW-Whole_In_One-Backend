package com.naturemobility.seoul.domain.payment.refund;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class GetRefundsRes extends DTOCommon {
    private Long storeIdx;
    private Long reservationIdx;
    private String merchantUid;
    private String refundReason;
    private int amount;
    private String userName;
    private String userPhoneNumber;
//    private int point;
    private String refundStatus;
    private Integer selectedHall;
    private Integer useTime;
    private Integer personCount;
    private String reservationTime;
    private Boolean isPaid;

    public GetRefundsRes(Long storeIdx) {
        this.storeIdx = storeIdx;
    }
}
