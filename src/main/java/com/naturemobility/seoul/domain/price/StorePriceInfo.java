package com.naturemobility.seoul.domain.price;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class StorePriceInfo extends PriceInfo {
    private Long StorePriceIdx;
    private LocalDate startDate;
    private LocalDate endDate;

    //join
    private String name;

    public StorePriceInfo(Long priceSchemeIdx, Boolean isHoliday) {
        super(priceSchemeIdx, isHoliday);
    }

    public StorePriceInfo(Long priceSchemeIdx, Integer price, LocalTime startTime, LocalTime endTime, Integer hole, Boolean isHoliday) {
        super(priceSchemeIdx, price, startTime, endTime, hole, isHoliday);
    }

    public StorePriceInfo(Long priceSchemeIdx, Integer price, LocalTime startTime, LocalTime endTime, Integer hole, Boolean isHoliday, Long storePriceIdx, LocalDate startDate, LocalDate endDate) {
        super(priceSchemeIdx, price, startTime, endTime, hole, isHoliday);
        StorePriceIdx = storePriceIdx;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public StorePriceInfo(Long priceSchemeIdx, Integer price, LocalTime startTime, LocalTime endTime, Integer hole, Boolean isHoliday, Long storePriceIdx) {
        super(priceSchemeIdx, price, startTime, endTime, hole, isHoliday);
        StorePriceIdx = storePriceIdx;
    }
}
