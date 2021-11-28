package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface StoresMapper {

    List<SearchStoreRes> findBySearch(@Param("storeName") String storeName, @Param("userLatitude") Double userLatitude, @Param("userLongitude")Double userLongitude);
    GetStoreRes retrieveStoreInfoByStoreIdx(@PathVariable("storeIdx") Long storeIdx);
    Integer checkStoreIdx (@PathVariable("storeIdx") Long storeIdx);
    List<GetStoreResByMap> retrieveStoreInfoByMap(@Param("userLatitude")Double userLatitude,@Param("userLongitude")Double userLongitude);
    void setOrderRule(@Param("orderRule")Integer orderRule);

    List<GetVisitedByUserIdx> retriveStoreInfoByUserIdx(@Param("userIdx") Long userIdx);
}
