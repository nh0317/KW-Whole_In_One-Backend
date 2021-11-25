package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRes {
    private final String storeImage;
    private final String storeBrand;
    private final String storeName;
    private final Float reviewStar;
    private final String storePhoneNumber;
    private final Integer reviewCount;
    private final String storeLocation;
    private final String storeTime;
    //private final String storePrice; -DB 업데이트후 추가 예정
}
