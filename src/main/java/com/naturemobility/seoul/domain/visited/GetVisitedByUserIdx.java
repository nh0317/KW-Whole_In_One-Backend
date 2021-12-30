package com.naturemobility.seoul.domain.visited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GetVisitedByUserIdx {
    private final Long storeIdx;
    private final String storeName;
    private final String storeImage;
    private final String storeType;
    private final Float reviewStar;
    private final Boolean reserveStatus;
    private final Boolean couponStatus;
}
