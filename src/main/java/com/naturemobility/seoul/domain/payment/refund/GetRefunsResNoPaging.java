package com.naturemobility.seoul.domain.payment.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetRefunsResNoPaging {
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
}
