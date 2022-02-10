package com.naturemobility.seoul.domain.price;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostPriceRes {
    private Long storePriceIdx;
    private String name;
    private Integer price;
    private String startTime;
    private String endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hole;
    private Boolean isHoliday;

    public PostPriceRes(Long storePriceIdx, String name, Integer price, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Integer hole, Boolean isHoliday) {
        this.storePriceIdx = storePriceIdx;
        this.name = name;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hole = hole;
        this.isHoliday = isHoliday;
        this.startTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}