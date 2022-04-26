package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.coupons.PostCouponInfo;
import com.naturemobility.seoul.domain.partnerStore.PostRoomInfo;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface PartnerStoreMapper {
    int save(StoreInfo storeInfo);

    int update(StoreInfo storeInfo);

    Optional<StoreInfo> findByStoreIdx(@Param("storeIdx") Long storeIdx, @Param("partnerIdx")Long partnerIdx);

    void postCouponInfo(PostCouponInfo postcouponInfo);

    Long getStoreIdxByCouponIdx(@Param("couponIdx") Long couponIdx);

    void deleteCouponInfo(@Param("couponIdx") Long couponIdx);

    int updateStoreImage(@Param("storeIdx") Long storeIdx, @Param("storeImage") String storeImage);

    void postRoomInfo(PostRoomInfo postRoomInfo);

    Long getPartnerIdxByRoomIdx(@Param("roomIdx") Long roomIdx);

    void deleteRoom(Long roomIdx);

    Long getStoreIdxByImgFileIdx(@Param("imgFileIdx") Long imgFileIdx);

    void deleteImgFile(@Param("imgFileIdx") Long imgFileIdx);
}
