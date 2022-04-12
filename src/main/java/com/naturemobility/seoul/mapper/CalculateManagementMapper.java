package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.calculateManagement.GetCalculationListRes;
import com.naturemobility.seoul.domain.calculateManagement.PostCalculation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface CalculateManagementMapper {
    Long getStoreIdx(Long partnerIdx);

    List<GetCalculationListRes> getCalculateList(@Param("partnerIdx") Long partnerIdx);

    List<GetCalculationListRes> getCalculateListWithFilter(
            @Param("partnerIdx")Long partnerIdx,@Param("startDate") String startDate,@Param("endDate")String endDate,
            @Param("calculationStatus") Integer calculationStatus);

    Long getStoreIdxByCalculationIdx(@PathVariable("calculationIdx") Long calculationIdx);

    Integer getCalculatedAmount(@Param("storeIdx") Long storeIdx, @Param("getCheckMonthRes") String getCheckMonthRes);

    void postCalculation(PostCalculation postCalculation);

    int checkDuplication(@Param("partnerIdx") Long partnerIdx, @Param("getCheckMonthRes") String getCheckMonthRes);

    int getCanceledAmount(@Param("storeIdx") Long storeIdx, @Param("getCheckMonthRes") String getCheckMonthRes);

    String getCheckMonth();

    Long getPartnerIdxFromPartnerPaymentIdx(@Param("partnerPaymentIdx") Long partnerPaymentIdx);

    void changeCalculateStatus(@Param("partnerPaymentIdx") Long partnerPaymentIdx);

    int checkCalculationDuplication(@Param("partnerPaymentIdx") Long partnerPaymentIdx);
}
