package com.naturemobility.seoul.domain.reservations;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@ToString
// 예약 테이블
public class ReservationInfo extends DTOCommon {

    // 예약 인덱스
    private Long reservationIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 유저 인덱스
    private Long userIdx;

    // 예약 시간
    private LocalDateTime reservationTime;

    // 결제 시간
    private LocalDateTime paymentTime;

    // 이용시간
    private Integer useTime;

    // 게임수
    private Integer numberOfGame;

    // 선택 홀
    private Integer selectedHall;

    // 가격
    private Integer reservePrice;
    private Integer payPrice;
    private Integer discountPrice;

    // 요청사항
    private String request;

    // 인원
    private Integer personCount;

    // 이용 여부
    private Boolean alreadyUsed;

    // 삭제한 시간
    private Date deletedAt;

    // 수정한 사람 idx
    private Long modiBy;

    //store join 시
    private String storeName;
    private int refundStatus;
    private String payMethod;
    private Double score;

    public ReservationInfo(Long userIdx) {
        this.userIdx = userIdx;
    }
}
