package com.naturemobility.seoul.domain.price;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class PostPriceReq {
    private String name;
    private Integer price;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hole;
    private Boolean isHoliday;
}
