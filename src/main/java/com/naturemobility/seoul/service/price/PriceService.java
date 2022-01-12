package com.naturemobility.seoul.service.price;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.price.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PriceService {
    PostWeekRes setWeek(PostWeekReq postWeek, Long partnerIdx) throws BaseException;
    PostPriceRes registerPrice(PostPriceReq postPriceReq, Long partnerIdx, Long storePriceIdx) throws BaseException;
    List<String> getWeek(Long partnerIdx, Boolean isHoliday)throws BaseException;
    List<GetPriceRes> getPrice(Long storeIdx, Boolean isHoliday) throws BaseException;
    List<GetPriceRes> getPeriodPrice(Boolean isHoilday, Long storeIdx) throws BaseException;

    void deletePrice(Long partnerIdx, Long storePriceIdx) throws BaseException;
    Integer getCurrentPrice(GetCurPriceReq getCurPriceReq, Long storeIdx) throws BaseException;
}
