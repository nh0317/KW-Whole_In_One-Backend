package com.naturemobility.seoul.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetMonthRes {
    final Integer reservationCount;
    final Integer monthSales;
}
