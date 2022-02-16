package com.naturemobility.seoul.mapper;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface CalculateManagementMapper
{
    Long getStoreIdx(Long partnerIdx);
    List<GetCalculateListRes> getCalculateList(@Param("storeIdx") Long storeIdx,@Param("startDay") String startDay,
                                               @Param("endDay")String endDay);

    Long getStoreIdxByCalculationIdx(@PathVariable("calculationIdx") Long calculationIdx);

    void changeCalculateStatus(@Param("calculationIdx") Long calculationIdx);
}
