package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreResByMap {
    private final Long storeIdx;
    private final String storeImage;
    private final String storeBrand;
    private final String storeName;
    private final Double storeLatitude;
    private final Double storeLongitude;
    private final Double distanceFromUser;
    private final Float reviewStar;

}
