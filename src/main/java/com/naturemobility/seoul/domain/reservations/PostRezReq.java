package com.naturemobility.seoul.domain.reservations;

import com.naturemobility.seoul.domain.payment.PaymentInfo;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostRezReq {
    private Long storeIdx;
    private String reservationTime;
    private Integer useTime;
    private String endTime;
    private Integer numberOfGame;
    private Long roomIdx;
    private Integer selectedHall;
    private String request;
    private Integer personCount;
    private Integer price;
    private Integer discountPrice;

    private String merchantUid;

    public PostRezReq(PostPayReq postPayReq, String merchantUid) {
        this.storeIdx = postPayReq.getStoreIdx();
        this.reservationTime = LocalDateTime.parse(postPayReq.getReservationTime(),
                DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA)).toString();
        this.useTime = postPayReq.getUseTime();
        if (postPayReq.getSelectedHall() == 18)
            this.numberOfGame = Double.valueOf((postPayReq.getUseTime()/60)).intValue();
        else if (postPayReq.getSelectedHall() == 9)
            this.numberOfGame = Double.valueOf((postPayReq.getUseTime()/30)).intValue();
        this.useTime = postPayReq.getUseTime();
        this.endTime = LocalDateTime.parse(postPayReq.getReservationTime(),
                        DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA))
                .plusMinutes(postPayReq.getUseTime()).toString();
        this.roomIdx = postPayReq.getRoomIdx();
        this.selectedHall = postPayReq.getSelectedHall();
        this.request = postPayReq.getRequest();
        this.personCount = postPayReq.getPersonCount();
        this.price = postPayReq.getAmount();
        this.discountPrice = postPayReq.getDiscountPrice();
        this.merchantUid = merchantUid;
    }

    public PostRezReq(PostGeneralPayReq postGeneralPayReq, String merchantUid) {
        this.storeIdx = postGeneralPayReq.getStoreIdx();
        this.reservationTime = LocalDateTime.parse(postGeneralPayReq.getReservationTime(),
                DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA)).toString();
        this.useTime = postGeneralPayReq.getUseTime();
        if (postGeneralPayReq.getSelectedHall() == 18)
            this.numberOfGame = Double.valueOf((postGeneralPayReq.getUseTime()/60)).intValue();
        else if (postGeneralPayReq.getSelectedHall() == 9)
            this.numberOfGame = Double.valueOf((postGeneralPayReq.getUseTime()/30)).intValue();
        this.useTime = postGeneralPayReq.getUseTime();
        this.endTime = LocalDateTime.parse(postGeneralPayReq.getReservationTime(),
                        DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA))
                .plusMinutes(postGeneralPayReq.getUseTime()).toString();
        this.roomIdx = postGeneralPayReq.getRoomIdx();
        this.selectedHall = postGeneralPayReq.getSelectedHall();
        this.request = postGeneralPayReq.getRequest();
        this.personCount = postGeneralPayReq.getPersonCount();
        this.price = postGeneralPayReq.getAmount();
        this.discountPrice = postGeneralPayReq.getDiscountPrice();
        this.merchantUid = merchantUid;

    }
}
