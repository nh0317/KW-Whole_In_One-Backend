package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.users.GetUsersRes;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.StoresMapper;
//import com.naturemobility.seoul.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;


@Service
@Slf4j
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoresMapper storesMapper;

    //@Autowired
    //private JwtService jwtService;


    public List<SearchStoreRes> retrieveStoreList(String storeName, Double userLatitude, Double userLongitude) throws BaseException {
        List<SearchStoreRes> storeInfoList;
        //storeInfoList = null;
        storeInfoList = storesMapper.findBySearch(storeName, userLatitude, userLongitude);
        return storeInfoList;
    }

    @Override
    public GetStoreRes retrieveStoreInfoByStoreIdx(Long storeIdx) throws BaseException {
        GetStoreRes storeInfo;
        storeInfo = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx);
        return storeInfo;
    }

    @Override
    public Integer checkStoreIdx(Long storeIdx) throws BaseException {
        Integer storeIdxCheck;
        storeIdxCheck = storesMapper.checkStoreIdx(storeIdx);
        return storeIdxCheck;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMap(Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(orderRule);
        storeInfoList = storesMapper.retrieveStoreInfoByMap(userLatitude,userLongitude);
        return storeInfoList;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter(StoreInfoReqByMap storeInfoReqByMap) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(storeInfoReqByMap.getOrderRule());
        storeInfoList = storesMapper.retrieveStoreInfoByMapWithFilter(storeInfoReqByMap.getUserLatitude(), storeInfoReqByMap.getUserLongitude(), storeInfoReqByMap.getBrand()
                , storeInfoReqByMap.getDistance(), storeInfoReqByMap.getFloorscreenStatus(), storeInfoReqByMap.getStorageStatus(), storeInfoReqByMap.getParkingStatus(), storeInfoReqByMap.getLessonStatus(),
                storeInfoReqByMap.getGroupseatStatus(),storeInfoReqByMap.getLefthandStatus()
                );
        return storeInfoList;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter2(Double userLatitude, Double userLongitude, Integer orderRule, Integer []brand,
                                                                    Integer lefthandStatus, Integer parkingStauts, Integer groupseatStatus, Integer floorscreenStatus,
                                                                    Integer storageStatus, Integer lessonStatus, Integer distance) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(orderRule);
        storeInfoList = storesMapper.retrieveStoreInfoByMapWithFilter2(userLatitude, userLongitude,brand, lefthandStatus, parkingStauts,
                groupseatStatus, floorscreenStatus, storageStatus, lessonStatus, distance);
        return storeInfoList;
    }

    @Override
    public List<GetBrandRes> retrieveBrandInfo() throws BaseException {
        List<GetBrandRes> brandInfoList;
        brandInfoList = storesMapper.retrieveBrandInfo();
        return brandInfoList;
    }

    @Override
    public List<GetRoomIdxRes> retrieveRoomIdx(Long storeIdx) throws BaseException {

        Long partnerIdx = 0L;
        List<GetRoomIdxRes> roomIdxRes;
        partnerIdx = storesMapper.retrievePartnerIdx(storeIdx);
        roomIdxRes = storesMapper.retrieveRoomIdx(partnerIdx);
        return roomIdxRes;
    }
}

