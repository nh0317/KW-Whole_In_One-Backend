package com.naturemobility.seoul.service.reservations;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.payment.RefundStatus;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import com.naturemobility.seoul.domain.reservations.ReservationInfo;
import com.naturemobility.seoul.domain.reservations.*;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.GetStoreResByMap;
import com.naturemobility.seoul.mapper.ReservationMapper;
import com.naturemobility.seoul.mapper.StoresMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    @Override
    public GetRezRes findByRezIdx(Long reservationIdx) throws BaseException {
        ReservationInfo reservation = reservationMapper.findByRezIdx(reservationIdx)
                .orElseThrow(()->new BaseException(NOT_FOUND_DATA));

        GetRezRes result = new GetRezRes(reservation);
        return result;
    }

    @Override
    public List<GetRezResByUserIdx> findByUserIdx(Long userIdx, Integer page)throws BaseException {
        List<ReservationInfo> reservationList = new ArrayList<>();
        ReservationInfo reservationInfo = new ReservationInfo(userIdx);
        Integer total = reservationMapper.cntTotal(userIdx);
        if(total != null && total >0) {
            if(page!=null && page > 1){
                reservationInfo.setPage(page);
            }
            PageInfo pageInfo = new PageInfo(reservationInfo);
            pageInfo.SetTotalData(total);
            reservationInfo.setPageInfo(pageInfo);

            if (page != null && page > reservationInfo.getPageInfo().getTotalPage()){
                return new ArrayList<>();
            }

            reservationList = reservationMapper.findAllByUserIdx(reservationInfo);

            return reservationList.stream().map( (r)->
                    new GetRezResByUserIdx(r.getReservationIdx(),r.getStoreIdx(),r.getStoreName(),
                            r.getReservationTime(), r.getUseTime(), r.getSelectedHall(),
                            r.getPersonCount(), r.getAlreadyUsed(), RefundStatus.getMsg(r.getRefundStatus()),
                            r.getScore())
            ).collect(Collectors.toList());
        }else if(page > total)
            return new ArrayList<>();
        else return new ArrayList<>();
    }

    @Override
    public Long getStoreIdx(Long reservationIdx) throws BaseException {
        Long storeIdx = 0L;
        storeIdx = reservationMapper.findStoreIdxByRezIdx(reservationIdx);
        return storeIdx;
    }

    @Override
    public void postReservation(PostRezReq postRezReq, Long userIdx) throws BaseException {
        try{
            Integer payPrice = postRezReq.getPrice() - postRezReq.getDiscountPrice();

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
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @Override
    public List<GetRezTime> getReservationTime(Long storeIdx, String reservationDay,Long roomIdx) throws BaseException {
        List<GetRezTime> getRezTime;
        getRezTime = reservationMapper.getReservationTime(storeIdx,reservationDay,roomIdx);
        return getRezTime;
    }

    @Override
    public Map<String,Integer> getTotalPage(Long userIdx){
        List<ReservationInfo> reservationList = new ArrayList<>();
        ReservationInfo reservationInfo = new ReservationInfo(userIdx);
        Integer total = reservationMapper.cntTotal(userIdx);
        Map<String, Integer> result = new HashMap<>();
        if(total != null && total >0) {
            PageInfo pageInfo = new PageInfo(reservationInfo);
            pageInfo.SetTotalData(total);
            reservationInfo.setPageInfo(pageInfo);

            result.put("totalPage", reservationInfo.getPageInfo().getTotalPage());
            return result;
        }
        result.put("totalPage", 0);
        return result;
    }

    @Override
    public GetRezResByStoreIdx getStoreInfo(Long storeIdx){
        GetStoreRes getStoreRes = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx);
        return new GetRezResByStoreIdx(getStoreRes.getStoreName(), getStoreRes.getStoreLocation());
    }

    @Override
    public List<GetCanRezTimeRes> getCanRezTimeRes(String reservationDate, Long storeIdx, Integer playTime){
        GetStoreRes getStoreRes = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx);
        List<String> storeTimes = List.of(getStoreRes.getStoreTime().split("~"));
        LocalTime startTime = LocalTime.parse(storeTimes.get(0).trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(storeTimes.get(1).trim(), DateTimeFormatter.ofPattern("HH:mm"));
        if (playTime != null) endTime = endTime.minusMinutes(playTime.longValue());
        else endTime =endTime.minusMinutes(30);
        log.info(storeTimes.get(0) + storeTimes.get(1));
        log.info(reservationDate);
        log.info("playTime : " + playTime + ", endTime : " + endTime);
        LocalTime firstTime;
        LocalTime secondTime;
        if (startTime.getMinute() > 30) {
            firstTime = LocalTime.of(startTime.getHour() + 1, 0);
            secondTime = LocalTime.of(startTime.getHour() + 1, 30);
        } else if (startTime.getMinute() > 0) {
            firstTime = LocalTime.of(startTime.getHour(), 30);
            secondTime = LocalTime.of(startTime.getHour() + 1, 0);
        } else {
            firstTime = LocalTime.of(startTime.getHour(), 0);
            secondTime = LocalTime.of(startTime.getHour(), 30);
        }
        log.info("firstTime : " + firstTime + ", secondTime : " + secondTime);
        try {
            List<GetCanRezTimeRes> canRezTime = reservationMapper.getCanRezTime(firstTime, secondTime,
                    reservationDate, endTime, storeIdx);
            return canRezTime;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
