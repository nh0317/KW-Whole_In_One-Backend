package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.users.GetUsersRes;

import java.util.List;

public interface StoreService {

    public List<SearchStoreRes> retrieveStoreList(String storeName,Double userLatitude,Double userLongitude) throws BaseException;
    public GetStoreRes retrieveStoreInfoByStoreIdx(Long storeIdx) throws BaseException;
    public Integer checkStoreIdx(Long storeIdx) throws BaseException;
    public List<GetStoreResByMap>retrieveStoreInfoByMap(Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException;
    public List<GetStoreResByMap>retrieveStoreInfoByMapWithFilter(StoreInfoReqByMap storeInfoReqByMap) throws BaseException;
    public List<GetStoreResByMap>retrieveStoreInfoByMapWithFilter2(Double userLatitude,Double userLongitude,Integer orderRule,Integer []brand,Integer lefthandStatus,
                                                                   Integer parkingStauts,Integer groupseatStatus,
                                                                   Integer floorscreenStatus,Integer storageStatus,Integer lessonStatus,Integer distance) throws BaseException;
}
