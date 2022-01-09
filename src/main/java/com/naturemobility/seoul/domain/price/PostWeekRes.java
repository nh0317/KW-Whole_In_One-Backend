package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostWeekRes {
//    Long weekPriceIdx;
    List<String> weeks;
    Boolean isHoliday;
}
