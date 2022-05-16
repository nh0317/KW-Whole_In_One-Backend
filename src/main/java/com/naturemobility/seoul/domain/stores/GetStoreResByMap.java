package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreResByMap {
    private Long storeIdx;
    private Integer reserveStatus;
    private Integer couponStatus;
    private String storeImage="https://i.ibb.co/h1kC5wc/image.png";
    private String storeBrand;
    private String storeName;
    private Double storeLatitude;
    private Double storeLongitude;
    private Double distanceFromUser;
    private  Float reviewStar;

}
