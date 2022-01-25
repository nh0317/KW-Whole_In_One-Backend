package com.naturemobility.seoul.mapper;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationManagementMapper
{
    Long getStoreIdx(Long userIdx);
    List<GetRezListByManagementRes> getRezList(@Param("reservationDay") String reservationDay,@Param("storeIdx")Long storeIdx);
}
