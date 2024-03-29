package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.coupons.CouponInfo;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.users.GetUsersRes;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.StoresMapper;
//import com.naturemobility.seoul.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
        Long partnerIdx = storesMapper.retrievePartnerIdx(storeIdx);
        return storesMapper.retrieveStoreInfoByStoreIdx(storeIdx, partnerIdx);
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
        storeInfoList = storesMapper.retrieveStoreInfoByMap(userLatitude, userLongitude);
        return storeInfoList;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter(StoreInfoReqByMap storeInfoReqByMap) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(storeInfoReqByMap.getOrderRule());
        storeInfoList = storesMapper.retrieveStoreInfoByMapWithFilter(storeInfoReqByMap.getUserLatitude(), storeInfoReqByMap.getUserLongitude(), storeInfoReqByMap.getBrand()
                , storeInfoReqByMap.getDistance(), storeInfoReqByMap.getFloorscreenStatus(), storeInfoReqByMap.getStorageStatus(), storeInfoReqByMap.getParkingStatus(), storeInfoReqByMap.getLessonStatus(),
                storeInfoReqByMap.getGroupseatStatus(), storeInfoReqByMap.getLefthandStatus()
        );
        return storeInfoList;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilterApplyAllBrand(StoreInfoReqByMap storeInfoReqByMap) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(storeInfoReqByMap.getOrderRule());
        storeInfoList = storesMapper.retrieveStoreInfoByMapWithFilter(storeInfoReqByMap.getUserLatitude(), storeInfoReqByMap.getUserLongitude(), storeInfoReqByMap.getBrand()
                , storeInfoReqByMap.getDistance(), storeInfoReqByMap.getFloorscreenStatus(), storeInfoReqByMap.getStorageStatus(), storeInfoReqByMap.getParkingStatus(), storeInfoReqByMap.getLessonStatus(),
                storeInfoReqByMap.getGroupseatStatus(), storeInfoReqByMap.getLefthandStatus()
        );
        return storeInfoList;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter2(Double userLatitude, Double userLongitude, Integer orderRule, Integer[] brand,
                                                                    Integer lefthandStatus, Integer parkingStauts, Integer groupseatStatus, Integer floorscreenStatus,
                                                                    Integer storageStatus, Integer lessonStatus, Integer distance) throws BaseException {

        List<GetStoreResByMap> storeInfoList;
        storesMapper.setOrderRule(orderRule);

        // facilityCheck
        boolean facilityCheck = (lefthandStatus == -1 || parkingStauts == -1 || groupseatStatus == -1 || floorscreenStatus == -1 || storageStatus == -1
                || lessonStatus == -1);

        // 브랜드만 조회
        if ((facilityCheck && (distance == -1)) == true) {
            //System.out.println("브랜드만 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithBrandFilter(userLatitude, userLongitude, brand);
        }

        // 시설만 조회
        else if ((brand.length == 0) && (distance == -1)) {
            //System.out.println("시설만 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithFacilityFilter(userLatitude, userLongitude, lefthandStatus, parkingStauts,
                    groupseatStatus, floorscreenStatus, storageStatus, lessonStatus);
        }

        // 거리만 조회
        else if ((brand.length == 0) && facilityCheck) {
            //System.out.println("거리만 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithDistanceFilter(userLatitude, userLongitude, distance);
        }

        // 브랜드 + 시설
        else if (distance == -1) {
            //System.out.println("브랜드+시설만 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithBrandFacilityFilter(userLatitude, userLongitude, brand, lefthandStatus, parkingStauts,
                    groupseatStatus, floorscreenStatus, storageStatus, lessonStatus);
        }

        // 브랜드 + 거리
        else if (facilityCheck) {
            //System.out.println("브랜드+거리만 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithBrandDistanceFilter(userLatitude, userLongitude, brand, distance);
        }

        //시설 + 거리
        else if (brand.length == 0) {
            //System.out.println("시설+거리 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithFacilityDistanceFilter(userLatitude, userLongitude, lefthandStatus, parkingStauts,
                    groupseatStatus, floorscreenStatus, storageStatus, lessonStatus, distance);
        } else {
            //System.out.println("전체 조회");
            storeInfoList = storesMapper.retrieveStoreInfoByMapWithFilter2(userLatitude, userLongitude, brand, lefthandStatus, parkingStauts,
                    groupseatStatus, floorscreenStatus, storageStatus, lessonStatus, distance);
        }
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

        for (GetRoomIdxRes i : roomIdxRes) {
            i.roomIdx = storesMapper.getRoomIdx(partnerIdx, i.getRoomType());
        }


        return roomIdxRes;
    }

    @Override
    public List<GetRoomNameRes> retrieveRoomName(Long storeIdx, String roomType) throws BaseException {
        Long partnerIdx = storesMapper.retrievePartnerIdx(storeIdx);

        if (roomType == null || roomType.equals(""))
            return storesMapper.retrieveRoomName(partnerIdx);
        else return storesMapper.retrieveRoomNameByRoomType(partnerIdx, roomType);
    }

    @Override
    public List<CouponInfo> retrieveCouponInfo(Long storeIdx) throws BaseException {
        List<CouponInfo> couponInfos = storesMapper.retrieveCouponInfo(storeIdx);
        return couponInfos;
    }

    @Override
    public List<Long> retrieveAvailableRoomIdx(Long storeIdx, String startTime, String endTime) throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = storesMapper.retrievePartnerIdx(storeIdx);

        List<Long> roomIdxOnlyRes = storesMapper.retrieveRoomIdxOnly(partnerIdx);

        List<Long> reservedRoomIdxRes = storesMapper.retrieveReservedRoomIdx(partnerIdx, startTime, endTime);
        //List<Long> reservedRoomIdxRes2 = storesMapper.retrieveReservedRoomIdx2(partnerIdx, endTime);

        roomIdxOnlyRes.removeAll(reservedRoomIdxRes);
        //roomIdxOnlyRes.removeAll(reservedRoomIdxRes2);

        return roomIdxOnlyRes;
    }

    @Override
    public List<GetStoreImagesRes> getStoreImagesRes(Long storeIdx) throws BaseException {
        List<GetStoreImagesRes> getStoreImagesRes = storesMapper.retrieveStoreImages(storeIdx);
        return getStoreImagesRes;
    }
}

