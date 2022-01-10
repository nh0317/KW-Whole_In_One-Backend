package com.naturemobility.seoul.mapper;
import com.naturemobility.seoul.domain.dashboard.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
}
