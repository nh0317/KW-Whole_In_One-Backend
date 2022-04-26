package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreRes {
    private String storeImage="https://i.ibb.co/h1kC5wc/image.png";
    private String storeBrand;
    private String storeName;
    private Float reviewStar;
    private String storePhoneNumber;
    private Integer reviewCount;
    private String storeLocation;
    private String storeTime;

    private Integer batCount;
    private Integer lefthandStatus;
    private Integer parkingStatus;
    private Integer groupseatStatus;
    private Integer floorscreenStatus;
    private Integer lessonStatus;
    //private final String storePrice; -DB 업데이트후 추가 예정z
}
