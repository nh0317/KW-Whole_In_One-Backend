package com.naturemobility.seoul.service.reservations;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.payment.RefundStatus;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.domain.reservations.ReservationInfo;
import com.naturemobility.seoul.domain.reservations.*;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.mapper.ReservationMapper;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.WeekPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class ReservationsServiceImpl implements ReservationsService {
    @Autowired
    ReservationMapper reservationMapper;

    @Autowired
    StoresMapper storesMapper;

    @Autowired
    WeekPriceMapper weekPriceMapper;

    @Override
    public GetRezRes findByRezIdx(Long reservationIdx) throws BaseException {
        ReservationInfo reservation = reservationMapper.findByRezIdx(reservationIdx)
                .orElseThrow(()->new BaseException(NOT_FOUND_DATA));

        GetRezRes result = new GetRezRes(reservation);
        return result;
    }

    @Override
    public List<GetRezResByUserIdx> findByUserIdx(Long userIdx, Integer page)throws BaseException {
        ReservationInfo reservationInfo = new ReservationInfo(userIdx);
        int total = reservationMapper.cntTotal(userIdx);
        List<GetRezResByUserIdx> result = new ArrayList<>();
        ReservationInfo pagingReservation = (ReservationInfo) reservationInfo.getPageInfo(page, total);
        try{
            if (pagingReservation != null) {
                List<ReservationInfo> reservationList = reservationMapper.findAllByUserIdx(pagingReservation);
                result = reservationList.stream().map((r) ->
                        new GetRezResByUserIdx(r.getReservationIdx(), r.getStoreIdx(), r.getStoreName(),
                                r.getReservationTime(), r.getUseTime(), r.getSelectedHall(),
                                r.getPersonCount(), r.getAlreadyUsed(), RefundStatus.getMsg(r.getRefundStatus()),
                                r.getScore())
                ).collect(Collectors.toList());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Long getStoreIdx(Long reservationIdx) throws BaseException {
        Long storeIdx = 0L;
        storeIdx = reservationMapper.findStoreIdxByRezIdx(reservationIdx);
        return storeIdx;
    }

    @Override
    public void postReservation(PostRezReq postRezReq, Long userIdx) throws BaseException {
        Integer payPrice = postRezReq.getPrice();

        String startTime = postRezReq.getReservationTime();
        String endTime = postRezReq.getEndTime();
        Long storeIdx = postRezReq.getStoreIdx();
        Long roomIdx = postRezReq.getRoomIdx();

        Integer checkDuplication1 = reservationMapper.checkDuplication1(startTime, endTime, storeIdx, roomIdx);
        Integer checkDuplication2 = reservationMapper.checkDuplication2(startTime, endTime, storeIdx, roomIdx);

        if (checkDuplication1 >= 1 || checkDuplication2 >= 1) {
            throw new BaseException(RESERVATION_DUPLICATION);
        }

        PostRezInfo postRezInfo = new PostRezInfo(userIdx, "Role_MEMBER", payPrice, postRezReq);
        reservationMapper.postReservation(postRezInfo);
        return;
    }

    @Override
    public void checkDuplication(PostCheckReservationReq postCheckRez) throws  BaseException{
        String endTime
                = LocalDateTime.parse(postCheckRez.getStartTime(), DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA))
                .plusMinutes(postCheckRez.getUseTime()).toString();
        String startTime
                = LocalDateTime.parse(postCheckRez.getStartTime(), DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA))
                .toString();
        int checkDuplication1 = reservationMapper.checkDuplication1(startTime, endTime,
                postCheckRez.getStoreIdx(), postCheckRez.getRoomIdx());
        int checkDuplication2 = reservationMapper.checkDuplication2(startTime,endTime,
                postCheckRez.getStoreIdx(), postCheckRez.getRoomIdx());
        if (checkDuplication1 >= 1 || checkDuplication2 >= 1) {
            throw new BaseException(RESERVATION_DUPLICATION);
        }
        return;
    }

    @Override
    public List<GetRezTime> getReservationTime(Long storeIdx, String reservationDay,Long roomIdx) throws BaseException {
        List<GetRezTime> getRezTime;
        getRezTime = reservationMapper.getReservationTime(storeIdx,reservationDay,roomIdx);
        return getRezTime;
    }

    @Override
    public Map<String,Integer> getTotalPage(Long userIdx){
        ReservationInfo reservationInfo = new ReservationInfo(userIdx);
        int total = reservationMapper.cntTotal(userIdx);
        reservationInfo.setTotalData(total);

        Map<String, Integer> result = new HashMap<>();
        result.put("totalPage", reservationInfo.getTotalPage());
        return result;
    }

    @Override
    public GetRezResByStoreIdx getStoreInfo(Long storeIdx){
        GetStoreRes getStoreRes = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx, storesMapper.retrievePartnerIdx(storeIdx));
        return new GetRezResByStoreIdx(getStoreRes.getStoreName(), getStoreRes.getStoreLocation());
    }

    @Override
    public List<GetCanRezTimeRes> getCanRezTimeRes(String reservationDate, Long storeIdx, Integer playTime,
                                                   Long roomIdx, Integer hall){
        GetStoreRes getStoreRes = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx, storesMapper.retrievePartnerIdx(storeIdx));
        List<String> storeTimes = List.of(getStoreRes.getStoreTime().split("~"));
        LocalTime startTime = LocalTime.parse(storeTimes.get(0).trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endLocalTime = LocalTime.parse(storeTimes.get(1).trim(),DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate reservationLocalDate = LocalDate.parse(reservationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime endTime;
        if(endLocalTime.isBefore(startTime)){
            endTime = LocalDateTime.of(reservationLocalDate.plusDays(1),endLocalTime);
        }else{
            endTime = LocalDateTime.of(reservationLocalDate, endLocalTime);
        }
        if (playTime != null && playTime != 0)
            endTime = endTime.minusMinutes(playTime.longValue());
        else endTime =endTime.minusMinutes(30);

        log.info(storeTimes.get(0) + storeTimes.get(1));
        log.info(reservationDate);
        log.info("playTime : " + playTime + ", endTime : " + endTime);

        if (startTime.getMinute() > 30) {
            startTime = LocalTime.of(startTime.getHour() + 1, 0);
        } else if (startTime.getMinute() > 0) {
            startTime = LocalTime.of(startTime.getHour(), 30);
        } else {
            startTime = LocalTime.of(startTime.getHour(), 0);
        }

        LocalDateTime firstTime = LocalDateTime.parse(reservationDate+" "+startTime.toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        log.info("firstTime : " + firstTime);
        List<GetRezTimes> canRezTime = reservationMapper.getCanRezTime(firstTime,
                reservationDate, endTime, storeIdx, roomIdx, hall,playTime);
        log.info(canRezTime.toString());
        return canRezTime.stream().filter((items)->items.isValid(playTime))
                .map((items)-> new GetCanRezTimeRes(items.getTime()))
                .collect(Collectors.toList());
    }
}
