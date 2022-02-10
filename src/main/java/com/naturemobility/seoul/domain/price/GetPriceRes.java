package com.naturemobility.seoul.domain.price;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GetPriceRes {
    private Long storePriceIdx;
    private String name;
    private Integer price;
    private String startTime;
    private String endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hole;
    private Boolean isHoliday;
}
