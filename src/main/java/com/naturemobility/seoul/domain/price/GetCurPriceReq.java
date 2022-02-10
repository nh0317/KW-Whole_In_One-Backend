package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCurPriceReq {
    private String date;
    private String time;
    private Integer hole;
    private Integer count;
    private Integer period;
}
