package com.naturemobility.seoul.domain.reservations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostRezInfo {
    private Long userIdx;
    private Integer storeIdx;
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

    public PostRezInfo(Long userIdx,Integer storeIdx,String reservationTime,Integer useTime,Integer numberOfGame,Integer selectedHall,String request,
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
}
