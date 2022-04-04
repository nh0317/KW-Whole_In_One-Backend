package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.calculateManagement.GetCalculationListRes;
import com.naturemobility.seoul.domain.calculateManagement.PostCalculation;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface CalculateManagementMapper {
    Long getStoreIdx(Long partnerIdx);

    List<GetCalculationListRes> getCalculateList(@Param("partnerIdx") Long partnerIdx);

    Long getStoreIdxByCalculationIdx(@PathVariable("calculationIdx") Long calculationIdx);

    void changeCalculateStatus(@Param("calculationIdx") Long calculationIdx);

    Integer getCalculatedAmount(@Param("storeIdx") Long storeIdx, @Param("calculationMonthDate") String calculationMonthDate);

    void postCalculation(PostCalculation postCalculation);

    int checkDuplication(@Param("partnerIdx") Long partnerIdx, @Param("calculationMonthDate") String calculationMonthDate);

    int getCanceledAmount(@Param("storeIdx") Long storeIdx, @Param("calculationMonthDate") String calculationMonthDate);
}
