package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.dashboard.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DashBoardMapper
{
    Long getStoreIdx(Long userIdx);
    GetTodayRes getTodayRes(Long storeIdx);
}
