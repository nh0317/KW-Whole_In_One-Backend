package com.naturemobility.seoul.service.dashboard;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.dashboard.*;
import com.naturemobility.seoul.mapper.DashBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    DashBoardMapper dashBoardMapper;

    @Override
    public GetTodayRes getTodayRes(Long partnerIdx) throws BaseException {
        GetTodayRes todayRes;
        Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
        todayRes = dashBoardMapper.getTodayRes(storeIdx);
        return todayRes;
    }

    @Override
    public List<GetRezListRes> getTodayRezList(Long partnerIdx) throws BaseException {
        List<GetRezListRes> todayRezList;
        Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
        todayRezList = dashBoardMapper.getTodayRezList(storeIdx);
        return todayRezList;
    }

    @Override
    public GetMonthRes getMonthRes(Long partnerIdx) throws BaseException {
        GetMonthRes monthRes;
        Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
        monthRes = dashBoardMapper.getMonthRes(storeIdx);
        return monthRes;
    }

    @Override
    public List<GetCalendarRes> getCalendarList(Long partnerIdx) throws BaseException {
        List<GetCalendarRes> calendarList;
        Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
        calendarList = dashBoardMapper.getCalendarList(storeIdx);
        return calendarList;
    }

    @Override
    public List<GetRezListRes> getSpecificRezList(Long partnerIdx, String reservationDay) throws BaseException {
        List<GetRezListRes> specificRezList;
        Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
        specificRezList = dashBoardMapper.getSpecificRezList(reservationDay,storeIdx);
        return specificRezList;
    }

    @Override
    public List<GetMemoRes> getMemoList(Long partnerIdx) throws BaseException {
        List<GetMemoRes> memoList;
        memoList = dashBoardMapper.getMemoList(partnerIdx);
        return memoList;
    }
}
