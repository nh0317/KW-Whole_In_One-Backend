package com.naturemobility.seoul.domain.partnerStore;

import com.naturemobility.seoul.domain.stores.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostPartnerStoreRes {
    private String storeName;
    private String storeInfo;
    private String storePhoneNumber;
    private String storeBrand;
    private String storeLocation;
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

    public PostPartnerStoreRes(StoreInfo storeInfo, String storeBrand) {
        this.storeName = storeInfo.getStoreName();
        this.storeInfo = storeInfo.getStoreInfo();
        this.storePhoneNumber = storeInfo.getStorePhoneNumber();
        this.storeLocation = storeInfo.getStoreLocation();
        this.storeTime = storeInfo.getStoreTime();
        this.batCount = storeInfo.getBatCount();
        this.lefthandStatus = storeInfo.getLefthandStatus();
        this.parkingStatus = storeInfo.getParkingStatus();
        this.groupSeatStatus = storeInfo.getGroupSeatStatus();
        this.floorScreenStatus = storeInfo.getFloorScreenStatus();
        this.storageStatus = storeInfo.getStorageStatus();
        this.lessonStatus = storeInfo.getLessonStatus();
        this.reserveStatus = storeInfo.getReserveStatus();
        this.couponStatus = storeInfo.getCouponStatus();
        this.storeBrand = storeBrand;
    }
}
