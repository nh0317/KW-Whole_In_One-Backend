package com.naturemobility.seoul.domain.reservations;

import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostRezInfo {
    private Long userIdx;
    private Long storeIdx;
    private String reservationTime;
    private Integer useTime;
    private Integer numberOfGame;
    private Long roomIdx;
    private Integer selectedHall;
    private String request;
    private Integer personCount;
    private Integer price;
    private Integer discountPrice;
    private Integer payPrice;
    private String endTime;
    private String merchantUid;
    private Long modiBy;
    private String modiRole;

    public PostRezInfo(Long userIdx,Long storeIdx,String reservationTime,Integer useTime,Integer numberOfGame,Integer selectedHall,String request,
                       Integer personCount,Integer price,Integer discountPrice,Integer payPrice,String endTime,Long roomIdx) {
        this.userIdx = userIdx;
        this.storeIdx = storeIdx;
        this.reservationTime = reservationTime;
        this.useTime = useTime;
        this.numberOfGame = numberOfGame;
        this.request = request;
        this.personCount = personCount;
        this. price = price;
        this. discountPrice = discountPrice;
        this.payPrice = payPrice;
        this.selectedHall = selectedHall;
        this.endTime = endTime;
        this.roomIdx = roomIdx;
    }

    public PostRezInfo(PostPayReq postPayReq, Long userIdx,String modiRole, String merchantUid) {
        this.userIdx = userIdx;
        this.storeIdx = postPayReq.getStoreIdx();
        this.reservationTime = LocalDateTime.parse(postPayReq.getReservationTime(),
                DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm")).toString();
        this.useTime = postPayReq.getUseTime();
        if (postPayReq.getSelectedHall() == 18)
            this.numberOfGame = Double.valueOf((postPayReq.getUseTime()/60)).intValue();
        else if (postPayReq.getSelectedHall() == 9)
            this.numberOfGame = Double.valueOf((postPayReq.getUseTime()/30)).intValue();
        this.request = postPayReq.getRequest();
        this.personCount = postPayReq.getPersonCount();
        this. price = postPayReq.getPrice();
        this. discountPrice = postPayReq.getDiscountPrice();
        this.payPrice = postPayReq.getAmount();
        this.selectedHall = postPayReq.getSelectedHall();
        this.endTime = LocalDateTime.parse(postPayReq.getReservationTime(),
                DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm"))
                .plusMinutes(postPayReq.getUseTime()).toString();
        this.roomIdx = postPayReq.getRoomIdx();
        this.merchantUid = merchantUid;
        this.modiBy = userIdx;
        this.modiRole = modiRole;
    }

    public PostRezInfo(Long userIdx, String modiRole, Integer payPrice, PostRezReq postRezReq) {
        this.userIdx = userIdx;
        this.storeIdx = postRezReq.getStoreIdx();
        this.reservationTime = postRezReq.getReservationTime();
        this.useTime = postRezReq.getUseTime();
        this.numberOfGame = postRezReq.getNumberOfGame();
        this.request = postRezReq.getRequest();
        this.personCount = postRezReq.getPersonCount();
        this.price = postRezReq.getPrice();
        this.discountPrice = postRezReq.getDiscountPrice();
        this.payPrice = payPrice;
        this.selectedHall = postRezReq.getSelectedHall();
        this.endTime = postRezReq.getEndTime();
        this.roomIdx = postRezReq.getRoomIdx();
        this.merchantUid = postRezReq.getMerchantUid();
        this.modiBy = userIdx;
        this.modiRole = modiRole;
    }
}
