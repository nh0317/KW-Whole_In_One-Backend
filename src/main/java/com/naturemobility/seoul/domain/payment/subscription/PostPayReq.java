package com.naturemobility.seoul.domain.payment.subscription;

import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayReq {
    private Long storeIdx;
    private Long couponIdx;
    private Long userPaymentIdx;
    private String payMethod;
    private Integer amount;
    private Integer point;

    private String reservationTime;
    private Integer useTime;
    private Long roomIdx;
    private Integer selectedHall;
    private String request;
    private Integer personCount;
    private Integer price;
    private Integer discountPrice;

    public PostPayReq(PostGeneralPayReq postGeneralPayReq) {
        this.storeIdx = postGeneralPayReq.getStoreIdx();
        this.couponIdx = postGeneralPayReq.getCouponIdx();
        this.userPaymentIdx = null;
        this.payMethod = postGeneralPayReq.getPayMethod();
        this.amount = postGeneralPayReq.getAmount();
        this.point = postGeneralPayReq.getPoint();
        this.reservationTime = postGeneralPayReq.getReservationTime();
        this.useTime = postGeneralPayReq.getUseTime();
        this.roomIdx = postGeneralPayReq.getRoomIdx();
        this.selectedHall = postGeneralPayReq.getSelectedHall();
        this.request = postGeneralPayReq.getRequest();
        this.personCount = postGeneralPayReq.getPersonCount();
        this.price = postGeneralPayReq.getPrice();
        this.discountPrice = postGeneralPayReq.getDiscountPrice();
    }
}
