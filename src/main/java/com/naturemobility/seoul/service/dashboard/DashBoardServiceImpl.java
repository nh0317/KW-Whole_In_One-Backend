package com.naturemobility.seoul.service.dashboard;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.dashboard.GetCalendarRes;
import com.naturemobility.seoul.domain.dashboard.GetMonthRes;
import com.naturemobility.seoul.domain.dashboard.GetRezListRes;
import com.naturemobility.seoul.domain.dashboard.GetTodayRes;
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
        try {
            Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
            todayRes = dashBoardMapper.getTodayRes(storeIdx);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return todayRes;
    }

    @Override
    public List<GetRezListRes> getTodayRezList(Long partnerIdx) throws BaseException {
        List<GetRezListRes> todayRezList;
        try {
            Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
            todayRezList = dashBoardMapper.getTodayRezList(storeIdx);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return todayRezList;
    }

    @Override
    public GetMonthRes getMonthRes(Long partnerIdx) throws BaseException {
        GetMonthRes monthRes;
        try {
            Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
            monthRes = dashBoardMapper.getMonthRes(storeIdx);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return monthRes;
    }

    @Override
    public List<GetCalendarRes> getCalendarList(Long partnerIdx) throws BaseException {
        List<GetCalendarRes> calendarList;
        try {
            Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
            calendarList = dashBoardMapper.getCalendarList(storeIdx);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return calendarList;
    }
}
