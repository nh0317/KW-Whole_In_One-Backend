package com.naturemobility.seoul.service.price;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.domain.price.*;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.WeekPriceMapper;
import com.naturemobility.seoul.utils.ExternalAPI;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
@Transactional
public class PriceServiceImpl implements PriceService{
    @Autowired
    WeekPriceMapper weekPriceMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    StoresMapper storesMapper;
    @Autowired
    SecretPropertyConfig secretPropertyConfig;

    @Override
    public PostWeekRes setWeek(PostWeekReq postWeek, Long partnerIdx) throws BaseException {
        Boolean isHoliday = postWeek.getIsHoliday();
        // 사장님의 매장을 찾는다.
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        //요청 온 요일을 평일 또는 주말로 설정
        setWeek(postWeek.getWeeks(),storeIdx, isHoliday);
        PostWeekRes postWeekRes = new PostWeekRes(postWeek.getWeeks(), isHoliday);
        return postWeekRes;
    }
    @Override
    @Transactional
    public PostPriceRes registerPrice(PostPriceReq postPriceReq, Long partnerIdx, Long storePriceIdx) throws BaseException {
        // 사장님의 가게를 찾는다.
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(NOT_FOUND_DATA));

        String date = "2022-01-01";
        //운영 시간 내의 시간인지 확인한다.
        GetStoreRes getStoreRes = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx);
        String[] storeTimes = getStoreRes.getStoreTime().split("~");
        LocalDateTime startTime = LocalDateTime.parse(date+" "+storeTimes[0].trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endTime = LocalDateTime.parse(date+" "+storeTimes[1].trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if(endTime.isBefore(startTime)) {
            endTime = endTime.plusDays(1);
        }

        if(postPriceReq.getPriceStartDateTime(date).isAfter(startTime)
                && postPriceReq.getPriceEndDateTime(date).isBefore(endTime)) {
            // 해당 가게에 가격을 등록한다.
            Long storeWeekPriceIdx = setPrice(postPriceReq, storeIdx, partnerIdx, storePriceIdx);
            return new PostPriceRes(storeWeekPriceIdx, postPriceReq.getName(), postPriceReq.getPrice(),
                    postPriceReq.getStartTime(), postPriceReq.getEndTime(), postPriceReq.getStartDate(),
                    postPriceReq.getEndDate(), postPriceReq.getHole(), postPriceReq.getIsHoliday());
        }
        else throw new BaseException(INVALID_TIME);
    }
    @Override
    public List<GetPriceRes> getPrice(Long storeIdx, Boolean isHoliday) throws BaseException{
        try {
//            Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
            List<GetPriceRes> priceInfos = weekPriceMapper.findWeekPriceByStoreIdx(isHoliday, storeIdx);
            return priceInfos;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<GetPriceRes> getPeriodPrice(Boolean isHoilday,Long storeIdx) throws BaseException {
        List<GetPriceRes> priceInfos = weekPriceMapper.findPeriodPriceByStoreIdx(isHoilday,storeIdx);
        return priceInfos;
    }

    @Override
    public List<String>getWeek(Long partnerIdx, Boolean isHoliday) throws BaseException {
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        String week = weekPriceMapper.findWeekByStoreIdx(isHoliday, storeIdx)
                .orElseGet(()-> null);
        List<String> weeks=new ArrayList<>();
        if(week!=null)
            weeks = Arrays.stream(week.split(",")).collect(Collectors.toList());
        return weeks;
    }

    @Override
    @Transactional
    public void deletePrice(Long partnerIdx, Long storePriceIdx) throws BaseException {
        Long storeIdx=partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        Long priceSchemeIdx = weekPriceMapper.findPriceSchemeByStoreIdx(storeIdx, storePriceIdx)
                .orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        weekPriceMapper.deletePriceScheme(priceSchemeIdx);
        weekPriceMapper.deleteStorePrice(storePriceIdx);
    }

    @Override
    public Integer getCurrentPrice(GetCurPriceReq getCurPriceReq, Long storeIdx) throws BaseException{

        LocalDate localDate = LocalDate.parse(getCurPriceReq.getDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));

        // 특정 기간의 가격에 포함되는 경우
        Integer currentPrice = weekPriceMapper.findCurrentPrice(storeIdx,
                        getCurPriceReq.getHole(), localDate, getCurPriceReq.getTime())
                        .orElseGet(()->{
            // 평일/주말 가격 탐색
            String currentWeek=localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).replace("요일", "");

            if (isHoliday(localDate)) {
                log.info(localDate.toString() + "은 공휴일");
                return weekPriceMapper.findCurrentWeekPrice(storeIdx, getCurPriceReq.getHole(),
                                getCurPriceReq.getTime(), true)
                        .orElseGet(() -> 0);
            }
            // 공휴일이 아닌 경우
            //매장의 주말 탐색
            String holiday = weekPriceMapper.findWeekByStoreIdx(true, storeIdx)
                    .orElseGet(()->null);
            String weekend = weekPriceMapper.findWeekByStoreIdx(false, storeIdx).orElseGet(() -> null);
            // 매장의 주말/평일 가격 반환(등록된 시간이 없는 경우 0)

            Integer price = 0;
            //주말인 경우
            if (holiday!=null && holiday.contains(currentWeek)) {
                price = weekPriceMapper.findCurrentWeekPrice(storeIdx, getCurPriceReq.getHole(),
                                getCurPriceReq.getTime(), true)
                        .orElseGet(() -> 0);
            }
            //평일인 경우
            else if (weekend != null && weekend.contains(weekend)){
                price = weekPriceMapper.findCurrentWeekPrice(storeIdx, getCurPriceReq.getHole(),
                                getCurPriceReq.getTime(), false)
                        .orElseGet(() -> 0);
            }
            //정기 휴무일인 경우
            else{
                return 0;
            }
            double hour = getCurPriceReq.getPeriod().doubleValue() / 60;
            log.info("현재 가격 : {} * {}", price,hour);
            return Double.valueOf((price * getCurPriceReq.getCount() * hour)).intValue();
        });
        double hour = getCurPriceReq.getPeriod().doubleValue() / 60;
        log.info("현재 가격 : {} * {}", currentPrice,hour);
        return Double.valueOf((currentPrice * getCurPriceReq.getCount() * hour)).intValue();
    }

    private boolean isHoliday(LocalDate localDate) {
        String stringUrl = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="
                + localDate.format(DateTimeFormatter.ofPattern("yyyy"))
                + "&solMonth=" + localDate.format(DateTimeFormatter.ofPattern("MM"))
                + "&serviceKey=" + secretPropertyConfig.getOpenAPIServiceKey();
        Map<String, Object> response =null;
        try {
            response = ExternalAPI.getXmlResponse(null,HttpMethod.GET,null, stringUrl);
        } catch (Exception e) {e.printStackTrace();}
        log.info(response.toString());
        ArrayList<Map<String, Object>> holidays;
        try{
            holidays = (ArrayList) ((Map) ((Map) ((Map) response.
                    getOrDefault("response", "none"))
                    .getOrDefault("body", "none"))
                    .getOrDefault("items", "none"))
                    .getOrDefault("item", new ArrayList<>());
        }catch (Exception e){
            //이번 달에 공휴일이 없는 경우
            return false;
        }

        for (Map h : holidays){
            String locdate = String.valueOf(((Double) h.getOrDefault("locdate", "none")).intValue());
            LocalDate holidayDate = LocalDate.parse(locdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            if(localDate.isEqual(holidayDate))
                return true;
        }
        return false;
    }

    //요일 등록
    private void setWeek(List<String>weeks, Long storeIdx, Boolean isHoliday) throws BaseException {
        HolidayWeekInfo holidayWeekInfo = new HolidayWeekInfo(storeIdx, isHoliday, StringUtils.join(weeks, ','));
        weekPriceMapper.saveHolidayWeek(holidayWeekInfo);
    }
    //가격 등록
    @Transactional
    @Override
    public Long setPrice(PostPriceReq postPriceReq, Long storeIdx, Long partnerIdx, Long storePriceIdx) throws BaseException {
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
            weekPriceMapper.savePriceScheme(priceSchemeInfo);
        }
        //가격 저장
        StorePriceInfo storePriceInfo = new StorePriceInfo(priceSchemeInfo.getPriceSchemeIdx(), postPriceReq.getPrice(),
                postPriceReq.getStartTime(), postPriceReq.getEndTime(), postPriceReq.getHole(), postPriceReq.getIsHoliday(),
                storePriceIdx, postPriceReq.getStartDate(), postPriceReq.getEndDate());
        weekPriceMapper.savePrice(storePriceInfo);
        return storePriceInfo.getStorePriceIdx();
    }
}
