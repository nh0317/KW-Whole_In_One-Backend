package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.coupons.PostCouponInfo;
import com.naturemobility.seoul.domain.stores.PostStoreReq;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface PartnerStoreMapper {
    int save(StoreInfo storeInfo);
    int update(StoreInfo storeInfo);
    Optional<StoreInfo> findByStoreIdx(@Param("storeIdx")Long storeIdx);
    void postCouponInfo(PostCouponInfo postcouponInfo);
}
