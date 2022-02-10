package com.naturemobility.seoul.domain.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
public class GetRezRes {
    final Long reservationIdx;
    final String storeName;
    final String reservationTIme; //yyyy.mm.dd 오전 hh:mm
    final String paymentTime; // yyyy.mm.dd
    final Integer useTime;
    final Integer selectedHall;
    final Integer personCount;
    final Boolean alreadyUsed;
    final Integer reservePrice;
    final Integer discountPrice;
    final Integer payPrice;

    public GetRezRes(Long reservationIdx, String storeName, LocalDateTime reservationTIme, LocalDateTime paymentTime,
                     Integer useTime, Integer selectedHall, Integer personCount, Boolean alreadyUsed, Integer reservePrice,
                     Integer discountPrice, Integer payPrice) {
        this.reservationIdx = reservationIdx;
        this.storeName = storeName;
        this.useTime = useTime;
        this.selectedHall = selectedHall;
        this.personCount = personCount;
        this.alreadyUsed = alreadyUsed;
        this.reservePrice = reservePrice;
        this.discountPrice = discountPrice;
        this.payPrice = payPrice;

        DateTimeFormatter format;
        format =  DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm");
        this.reservationTIme = reservationTIme.format(format);
        format =  DateTimeFormatter.ofPattern("yyyy.MM.dd");
        this.paymentTime = paymentTime.format(format);
    }
}
