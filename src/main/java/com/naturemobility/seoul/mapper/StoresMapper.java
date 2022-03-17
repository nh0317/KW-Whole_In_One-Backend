package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.coupons.CouponInfo;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StoresMapper {

    List<SearchStoreRes> findBySearch(@Param("storeName") String storeName, @Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude);

    GetStoreRes retrieveStoreInfoByStoreIdx(@PathVariable("storeIdx") Long storeIdx);

    Integer checkStoreIdx(@PathVariable("storeIdx") Long storeIdx);

    List<GetStoreResByMap> retrieveStoreInfoByMap(@Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude);

    void setOrderRule(@Param("orderRule") Integer orderRule);

    List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter(@Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude, @Param("brand") Integer brand,
                                                            @Param("lefthandStatus") Integer lefthandStatus, @Param("parkingStatus") Integer parkingStatus, @Param("groupseatStatus") Integer groupseatStatus,
                                                            @Param("groupseatStatus") Integer groupStatus, @Param("floorscreenStatus") Integer floorscreenStatus,
                                                            @Param("storageStatus") Integer storageStatus, @Param("distance") Integer distance);

    List<GetVisitedByUserIdx> retriveStoreInfoByUserIdx(@Param("userIdx") Long userIdx);

    List<GetStoreResByMap> retrieveStoreInfoByMapWithFilter2(@Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude, @Param("brand") Integer[] brand,
                                                             @Param("lefthandStatus") Integer lefthandStatus, @Param("parkingStatus") Integer parkingStatus, @Param("groupseatStatus") Integer groupseatStatus,
                                                             @Param("floorscreenStatus") Integer floorscreenStatus, @Param("storageStatus") Integer storageStatus, @Param("lessonStatus") Integer lessonStatus,
                                                             @Param("distance") Integer distance);

    List<GetBrandRes> retrieveBrandInfo();

    Optional<String> findStoreName(@Param("storeIdx") Long storeIdx);

    Long retrievePartnerIdx(@Param("storeIdx") Long storeIdx);

    List<GetRoomIdxRes> retrieveRoomIdx(Long partnerIdx);

    List<CouponInfo> retrieveCouponInfo(@Param("storeIdx") Long storeIdx);

    List<Long> retrieveReservedRoomIdx(@Param("partnerIdx") Long partnerIdx,
                                       @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Long> retrieveReservedRoomIdx2(@Param("partnerIdx") Long partnerIdx,
                                        @Param("endTime") String endTime);

    List<Long> retrieveRoomIdxOnly(@Param("partnerIdx") Long partnerIdx);
}
