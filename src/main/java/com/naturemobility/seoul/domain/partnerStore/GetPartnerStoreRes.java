package com.naturemobility.seoul.domain.partnerStore;

import com.naturemobility.seoul.domain.stores.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetPartnerStoreRes {
    private String storeName;
    private String storeInfo;
    private String storePhoneNumber;
    private String storeBrand;
    private String storeLocation;
    private String mainStoreImage;
    private List<String> storeImage;
    private String storeTime;
    private Integer batCount;
    private Boolean lefthandStatus;
    private Boolean parkingStatus;
    private Boolean groupSeatStatus;
    private Boolean floorScreenStatus;
    private Boolean storageStatus;
    private Boolean lessonStatus;
    private Boolean reserveStatus;
    private Boolean couponStatus;

    public GetPartnerStoreRes(StoreInfo storeInfo, String storeBrand, List<String> storeImage) {
        this.storeName = storeInfo.getStoreName();
        this.storeInfo = storeInfo.getStoreInfo();
        this.storePhoneNumber = storeInfo.getStorePhoneNumber();
        this.storeBrand = storeBrand;
        this.storeLocation = storeInfo.getStoreLocation();
        this.mainStoreImage = storeInfo.getStoreImage();
        this.storeImage = storeImage;
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
    }
}
