package com.naturemobility.seoul.service.dashboard;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.dashboard.*;
import java.util.List;

public interface DashBoardService {
    public GetTodayRes getTodayRes(Long partnerIdx) throws BaseException;
    public List<GetRezListRes> getTodayRezList(Long partnerIdx) throws BaseException;
    public GetMonthRes getMonthRes(Long partnerIdx) throws BaseException;
    public List<GetCalendarRes> getCalendarList(Long partnerIdx) throws BaseException;
    public List<GetRezListRes> getSpecificRezList(Long partnerIdx,String reservationDay) throws BaseException;

}
