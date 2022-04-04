package com.naturemobility.seoul.domain.price;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    public LocalDateTime getPriceStartDateTime(String date){
        return LocalDateTime.parse(date +" "
                        + startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public LocalDateTime getPriceEndDateTime(String date){
        LocalDateTime endDateTime = LocalDateTime.parse(date+" "
                        + endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if(endTime.isBefore(startTime)) {
            endDateTime = endDateTime.plusDays(1);
        }
        return endDateTime;
    }
}
