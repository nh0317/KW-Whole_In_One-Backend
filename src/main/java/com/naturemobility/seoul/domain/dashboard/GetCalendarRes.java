package com.naturemobility.seoul.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCalendarRes {
    final String date;
    final Integer count;
}
