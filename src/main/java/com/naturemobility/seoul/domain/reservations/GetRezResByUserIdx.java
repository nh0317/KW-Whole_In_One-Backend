package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Getter
public class GetRezResByUserIdx {
    final Long reservationIdx;
    final Long storeIdx;
    final String storeName;
    final String reservationTime;
    final Integer useTime;
    final Integer selectHall;
    final Integer personCount;
    final Boolean alreadyUsed;

    public GetRezResByUserIdx(Long reservationIdx, Long storeIdx, String storeName, LocalDateTime reservationTime, Integer useTime, Integer selectHall, Integer personCount, Boolean alreadyUsed) {
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
    }
}
