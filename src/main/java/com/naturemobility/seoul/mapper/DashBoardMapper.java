package com.naturemobility.seoul.mapper;
import com.naturemobility.seoul.domain.dashboard.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DashBoardMapper
{
    Long getStoreIdx(Long userIdx);
    GetTodayRes getTodayRes(Long storeIdx);
    GetMonthRes getMonthRes(Long storeIdx);
    List<GetRezListRes> getTodayRezList(Long storeIdx);
    List<GetCalendarRes> getCalendarList(Long storeIdx);
    List<GetRezListRes> getSpecificRezList(@Param("reservationDay") String reservationDay,@Param("storeIdx")Long storeIdx);
    List<GetMemoRes> getMemoList(Long partnerIdx);
    void postMemo(PostMemoInfo postMemoInfo);
}
