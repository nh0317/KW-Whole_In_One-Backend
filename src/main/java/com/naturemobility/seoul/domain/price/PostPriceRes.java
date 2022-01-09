package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class PostPriceRes {
    private Long storePriceIdx;
    private String name;
    private Integer price;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hole;
    private Boolean isHoliday;
}