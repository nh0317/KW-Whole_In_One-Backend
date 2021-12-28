package com.naturemobility.seoul.domain.reservations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostRezReq {
    private Integer storeIdx;
    private String reservationTime;
    private Integer useTime;
    private Integer numberOfGame;
    private Integer selectedHall;
    private String request;
    private Integer personCount;
    private Integer price;
    private Integer discountPrice;
}
