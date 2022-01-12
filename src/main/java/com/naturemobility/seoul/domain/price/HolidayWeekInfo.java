package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HolidayWeekInfo {
    Long storeIdx;
    Boolean isHoliday;
    String week;
}
