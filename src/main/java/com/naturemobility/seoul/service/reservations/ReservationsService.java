package com.naturemobility.seoul.service.reservations;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservations.*;
import java.util.List;
import java.util.Map;

public interface ReservationsService {
    public GetRezRes findByRezIdx(Long reservationIdx) throws BaseException;
    public List<GetRezResByUserIdx> findByUserIdx(Long userIdx, Integer page) throws BaseException;

    public Long getStoreIdx(Long reservationIdx) throws BaseException;

    public void postReservation(PostRezReq postRezReq,Long userIdx) throws BaseException;
    
    public List<GetRezTime> getReservationTime(Long storeIdx,String reservationDay,Long hallNumber) throws BaseException;

    Map<String,Integer> getTotalPage(Long userIdx);
}
