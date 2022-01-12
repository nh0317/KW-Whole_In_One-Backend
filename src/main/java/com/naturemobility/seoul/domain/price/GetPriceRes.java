package com.naturemobility.seoul.domain.price;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class GetPriceRes {
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
