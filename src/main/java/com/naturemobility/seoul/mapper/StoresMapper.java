package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.domain.users.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoresMapper {

    List<StoreInfo> searchStoreByName(SearchStoreRes searchStoreRes);
    List<UserInfo> findByStatus(@Param("status") Integer status);
    List<UserInfo> findByStatusAndNicknameIsContaining(@Param("status") Integer status, @Param("word") String word);
    List<SearchStoreRes> findBySearch(@Param("storeName") String storeName, @Param("userLatitude") Double userLatitude, @Param("userLongitude")Double userLongitude);

}
