package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.domain.users.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface StoresMapper {

    List<SearchStoreRes> findBySearch(@Param("storeName") String storeName, @Param("userLatitude") Double userLatitude, @Param("userLongitude")Double userLongitude);
    GetStoreRes retrieveStoreInfoByStoreIdx(@PathVariable("storeIdx") Integer storeIdx);
    Integer checkStoreIdx (@PathVariable("storeIdx") Integer storeIdx);
}
