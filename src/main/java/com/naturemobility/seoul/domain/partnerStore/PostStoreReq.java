package com.naturemobility.seoul.domain.partnerStore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class PostStoreReq {
    private String storeName;
    private String storeInfo;
    private String storePhoneNumber;
    private String storeBrand;
    private String storeLocation;
//    private String mainStoreImage;
//    private List<String> storeImage;
    private String storeTime;
    private String batCount;
    private Boolean lefthandStatus;
    private Boolean parkingStatus;
    private Boolean groupSeatStatus;
    private Boolean floorScreenStatus;
    private Boolean storageStatus;
    private Boolean lessonStatus;
    private Boolean reserveStatus;
    private Boolean couponStatus;
}
