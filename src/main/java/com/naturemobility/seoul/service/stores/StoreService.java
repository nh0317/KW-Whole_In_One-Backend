package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.GetStoreResByMap;
import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.domain.users.GetUsersRes;

import java.util.List;

public interface StoreService {

    public List<SearchStoreRes> retrieveStoreList(String storeName,Double userLatitude,Double userLongitude) throws BaseException;
    public GetStoreRes retrieveStoreInfoByStoreIdx(Long storeIdx) throws BaseException;
    public Integer checkStoreIdx(Long storeIdx) throws BaseException;
    public List<GetStoreResByMap>retrieveStoreInfoByMap(Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException;

}
