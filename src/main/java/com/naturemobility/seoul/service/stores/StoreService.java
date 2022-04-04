package com.naturemobility.seoul.service.stores;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.coupons.CouponInfo;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.users.GetUsersRes;

import java.util.List;

public interface StoreService {

    public List<SearchStoreRes> retrieveStoreList(String storeName, Double userLatitude, Double userLongitude) throws BaseException;

    public GetStoreRes retrieveStoreInfoByStoreIdx(Long storeIdx) throws BaseException;

    public Integer checkStoreIdx(Long storeIdx) throws BaseException;

    public List<GetStoreResByMap> retrieveStoreInfoByMap(Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException;

    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter(StoreInfoReqByMap storeInfoReqByMap) throws BaseException;

    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilterApplyAllBrand(StoreInfoReqByMap storeInfoReqByMap) throws BaseException;

    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter2(Double userLatitude, Double userLongitude, Integer orderRule, Integer[] brand, Integer lefthandStatus,
                                                                    Integer parkingStatus, Integer groupseatStatus,
                                                                    Integer floorscreenStatus, Integer storageStatus, Integer lessonStatus, Integer distance) throws BaseException;

    public List<GetBrandRes> retrieveBrandInfo() throws BaseException;

    public List<GetRoomIdxRes> retrieveRoomIdx(Long storeIdx) throws BaseException;

    public List<CouponInfo> retrieveCouponInfo(Long storeIdx) throws BaseException;

    public List<Long> retrieveAvailableRoomIdx(Long storeIdx, String startTime, String endTime) throws BaseException;

    public List<GetStoreImagesRes> getStoreImagesRes(Long storeIdx) throws BaseException;
}
