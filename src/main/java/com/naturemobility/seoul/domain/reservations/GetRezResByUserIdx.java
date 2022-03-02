package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Getter
@NoArgsConstructor
public class GetRezResByUserIdx {
    private Long reservationIdx;
    private Long storeIdx;
    private String storeName;
    private String reservationTime;
    private Integer useTime;
    private Integer selectHall;
    private Integer personCount;
    private Boolean alreadyUsed;
    private String refundStatus;

    public GetRezResByUserIdx(Long reservationIdx, Long storeIdx, String storeName, LocalDateTime reservationTime, Integer useTime, Integer selectHall, Integer personCount, Boolean alreadyUsed, String refundStatus) {
        this.reservationIdx = reservationIdx;
        this.storeIdx = storeIdx;
        this.storeName = storeName;
        this.useTime = useTime;
        this.selectHall = selectHall;
        this.personCount = personCount;
        this.alreadyUsed = alreadyUsed;
        DateTimeFormatter format;
        format =  DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm");
        this.reservationTime = reservationTime.format(format);
        this.refundStatus = refundStatus;
    }
}
