package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.partnerStore.GetStoreStarPointRes;
import com.naturemobility.seoul.domain.partners.PartnerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PartnerMapper {
    int save(PartnerInfo partnerInfo);
    int update(PartnerInfo partnerInfo);
    int delete(PartnerInfo partnerInfo);
    Optional<PartnerInfo> findByIdx(@Param("partnerIdx") Long partnerIdx);
    List<PartnerInfo> findByStatus(@Param("status") Integer status);
    List<PartnerInfo> findByEmail(@Param("email") String email);
    List<PartnerInfo> findByEmailAndStatus(@Param("email") String email, @Param("status") Integer status);

    Optional<Long> findStoreIdx(@Param("partnerIdx")Long partnerIdx);
    Long saveStoreIdx(@Param("partnerIdx") Long partnerIdx, @Param("storeIdx") Long storeIdx);
    GetStoreStarPointRes getStoreStarPoint(Long storeIdx);
}
