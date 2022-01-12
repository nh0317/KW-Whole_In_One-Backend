package com.naturemobility.seoul.domain.price;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class PostWeekReq {
    Boolean isHoliday;
    List<String> weeks;
}
