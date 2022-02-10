package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
public abstract class PriceInfo {
    // 가격 스키마 인덱스
    private Long priceSchemeIdx;

    // 가격
    private Integer price;

    // 시작 시간
    private Time startTime;

    // 종료 시간
    private Time endTime;

    private Integer hole;

    // 주말 0: 평일 1: 주말
    private Boolean isHoliday;

    public PriceInfo(Long priceSchemeIdx, Boolean isHoliday) {
        this.priceSchemeIdx = priceSchemeIdx;
        this.isHoliday = isHoliday;
    }

    public PriceInfo(Long priceSchemeIdx, Integer price, LocalTime startTime, LocalTime endTime, Integer hole, Boolean isHoliday) {
        this.priceSchemeIdx = priceSchemeIdx;
        this.price = price;
        this.startTime = Time.valueOf(startTime);
        this.endTime = Time.valueOf(endTime);
        this.hole = hole;
        this.isHoliday = isHoliday;
    }
}
