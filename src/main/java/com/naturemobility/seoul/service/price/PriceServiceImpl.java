package com.naturemobility.seoul.service.price;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.price.*;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.WeekPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@Service
@Slf4j
@Transactional
public class PriceServiceImpl implements PriceService{
    @Autowired
    WeekPriceMapper weekPriceMapper;
    @Autowired
    PartnerMapper partnerMapper;

    @Override
    public PostWeekRes setWeek(PostWeekReq postWeek, Long partnerIdx) throws BaseException {
        Boolean isHoliday = postWeek.getIsHoliday();
        final List<String> weeks = new ArrayList<>(Arrays.asList("월", "화", "수", "목", "금", "토", "일"));
        // 사장님의 매장을 찾는다.
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));
        //요청 온 요일을 평일 또는 주말로 설정
        setWeek(postWeek.getWeeks(),storeIdx, isHoliday);
        PostWeekRes postWeekRes = new PostWeekRes(postWeek.getWeeks(), isHoliday);
        return postWeekRes;
    }
    @Override
    public PostPriceRes registerPrice(PostPriceReq postPriceReq, Long partnerIdx, Long storePriceIdx) throws BaseException {
        // 사장님의 가게를 찾는다.
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));
        // 해당 가게에 가격을 등록한다.
        Long storeWeekPriceIdx = setPrice(postPriceReq, storeIdx, partnerIdx,storePriceIdx);
        return new PostPriceRes(storeWeekPriceIdx, postPriceReq.getName(), postPriceReq.getPrice(),
                postPriceReq.getStartTime(), postPriceReq.getEndTime(),postPriceReq.getStartDate(),
                postPriceReq.getEndDate(), postPriceReq.getHole(), postPriceReq.getIsHoliday());
    }
    @Override
    public List<GetPriceRes> getPrice(Long partnerIdx, Boolean isHoliday) throws BaseException{
        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));
        List<GetPriceRes> priceInfos = weekPriceMapper.findWeekPriceByStoreIdx(isHoliday,storeIdx);
        return priceInfos;
    }

    @Override
    public List<GetPriceRes> getPeriodPrice(Boolean isHoilday,Long storeIdx) throws BaseException {
        List<GetPriceRes> priceInfos = weekPriceMapper.findPeriodPriceByStoreIdx(isHoilday,storeIdx);
        return priceInfos;
    }

    @Override
    public List<String>getWeek(Long partnerIdx, Boolean isHoliday) throws BaseException {
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));
        String week = weekPriceMapper.findWeekByStoreIdx(isHoliday, storeIdx)
                .orElseThrow(() -> new BaseException(RESPONSE_ERROR));
        List<String> weeks = Arrays.stream(week.split(",")).collect(Collectors.toList());
        return weeks;
    }

    @Override
    public void deletePrice(Long partnerIdx, Long storePriceIdx) throws BaseException {
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));
        Long priceSchemeIdx = weekPriceMapper.findPriceSchemeByStoreIdx(storeIdx, storePriceIdx)
                .orElseThrow(()->new BaseException(RESPONSE_ERROR));
        weekPriceMapper.deletePriceScheme(priceSchemeIdx);
    }

    @Override
    public Integer getCurrentPrice(GetCurPriceReq getCurPriceReq, Long storeIdx) throws BaseException {
        // 특정 기간의 가격에 포함되는 경우
        LocalDate localDate = LocalDate.parse(getCurPriceReq.getDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));
        Integer currentPrice = weekPriceMapper.findCurrentPrice(storeIdx,
                        getCurPriceReq.getHole(), localDate, getCurPriceReq.getTime())
                        .orElseGet(()->{
            //
            // 평일/주말 가격 탐색
            String currentWeek=localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).replace("요일", "");
            String holiday="";
            //TODO: 공휴일 판단 로직 추가

            // 공휴일이 아닌 경우
            try {
                //매장의 주말 탐색
                holiday= weekPriceMapper.findWeekByStoreIdx(true, storeIdx)
                        .orElseThrow(()->new BaseException(RESPONSE_ERROR));
                // 매장의 주말/평일 가격 반환(등록된 시간이 없는 경우 0)
                Integer price = weekPriceMapper.findCurrentWeekPrice(storeIdx, getCurPriceReq.getHole(),
                                getCurPriceReq.getTime(),holiday.contains(currentWeek))
                        .orElseGet(()->0);
                log.info("현재 가격 : {} ", price);
                Integer hour = Integer.parseInt(getCurPriceReq.getTime().split(":")[0])
                        + (Integer.parseInt(getCurPriceReq.getTime().split(":")[1])/60);
                return price * getCurPriceReq.getCount() * hour;
            } catch (BaseException e) {}
            return 0;
        });
        return currentPrice;
    }

    //요일 등록
    private void setWeek(List<String>weeks, Long storeIdx, Boolean isHoliday) throws BaseException {
        HolidayWeekInfo holidayWeekInfo = new HolidayWeekInfo(storeIdx, isHoliday, StringUtils.join(weeks, ','));
        weekPriceMapper.saveHolidayWeek(holidayWeekInfo);
    }
    //가격 등록
    private Long setPrice(PostPriceReq postPriceReq, Long storeIdx, Long partnerIdx,Long storePriceIdx) throws BaseException {
        PriceSchemeInfo priceSchemeInfo = new PriceSchemeInfo(storeIdx, postPriceReq.getName(), partnerIdx);
        if (storePriceIdx == null) {
            // 가격을 새로 등록하는 경우
            // 가격 스키마를 추가
            weekPriceMapper.savePriceScheme(priceSchemeInfo);
        } else {
            // 가격을 수정하는 경우
            // 기존 가격 스키마를 찾는다.
            priceSchemeInfo.setPriceSchemeIdx(weekPriceMapper.findPriceSchemeByStoreIdx(storeIdx, storePriceIdx)
                    .orElseThrow(() -> new BaseException(RESPONSE_ERROR)));
        }
        //가격 저장
        StorePriceInfo storePriceInfo = new StorePriceInfo(priceSchemeInfo.getPriceSchemeIdx(), postPriceReq.getPrice(),
                postPriceReq.getStartTime(), postPriceReq.getEndTime(), postPriceReq.getHole(), postPriceReq.getIsHoliday(),
                storePriceIdx, postPriceReq.getStartDate(), postPriceReq.getEndDate());
        weekPriceMapper.savePrice(storePriceInfo);
        return storePriceInfo.getStorePriceIdx();
    }
}
