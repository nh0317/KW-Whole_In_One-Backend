package com.naturemobility.seoul.service.reservations;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import com.naturemobility.seoul.domain.reservations.ReservationInfo;
import com.naturemobility.seoul.domain.reservations.*;
import com.naturemobility.seoul.domain.stores.GetStoreResByMap;
import com.naturemobility.seoul.mapper.ReservationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class ReservationsServiceImpl implements ReservationsService {
    @Autowired
    ReservationMapper reservationMapper;

    private static final int PAYMENT_TIME=1;
    private static final int RESERVATION_TIME=2;
    private static final int RESERVATION_DETAIL=3;

    @Override
    public GetRezRes findByRezIdx(Long reservationIdx) throws BaseException {
        ReservationInfo reservation;
        reservation = reservationMapper.findByRezIdx(reservationIdx).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        String reservationTime = changeDateFormat(reservation.getReservationTime(),RESERVATION_TIME);
        String paymentTime = changeDateFormat(reservation.getPaymentTime(), PAYMENT_TIME);

        GetRezRes result = new GetRezRes(reservation.getReservationIdx(), reservation.getStoreName(),
                reservationTime, paymentTime, reservation.getUseTime(),
                reservation.getSelectedHall(), reservation.getPersonCount(), reservation.getAlreadyUsed(),
                reservation.getReservePrice(), reservation.getDiscountPrice(), reservation.getPayPrice());
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
            reservationList = reservationMapper.findAllByUserIdx(reservationInfo);

            return reservationList.stream().map( (r)->
                    new GetRezResByUserIdx(r.getReservationIdx(),r.getStoreName(),
                            changeDateFormat(r.getReservationTime(),RESERVATION_DETAIL),
                            r.getUseTime(), r.getSelectedHall(), r.getPersonCount(), r.getAlreadyUsed())
            ).collect(Collectors.toList());
        }else throw new BaseException(NOT_FOUND_DATA);
    }

    @Override
    public Long getStoreIdx(Long reservationIdx) throws BaseException {
        Long storeIdx = 0L;
        storeIdx = reservationMapper.findStoreIdxByRezIdx(reservationIdx);
        return storeIdx;
    }

    @Override
    public void postReservation(PostRezReq postRezReq, Long userIdx) throws BaseException {
        Integer payPrice = postRezReq.getPrice() - postRezReq.getDiscountPrice();
        PostRezInfo postRezInfo = new PostRezInfo(userIdx,postRezReq.getStoreIdx(),postRezReq.getReservationTime(),postRezReq.getUseTime(),
                postRezReq.getNumberOfGame(),postRezReq.getSelectedHall(),postRezReq.getRequest(),postRezReq.getPersonCount(),
                postRezReq.getPrice(),postRezReq.getDiscountPrice(),payPrice,postRezReq.getEndTime());
        reservationMapper.postReservation(postRezInfo);
        return;
    }

    @Override
    public List<GetRezTime> getReservationTime(Long storeIdx, String reservationDay,Long hallNumber) throws BaseException {
        List<GetRezTime> getRezTime;
        getRezTime = reservationMapper.getReservationTime(storeIdx,reservationDay,hallNumber);
        return getRezTime;
    }

    public String changeDateFormat(Date date, int type) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = transFormat.parse(dateStr);
        String result="";
        switch (type) {
            case PAYMENT_TIME:
                transFormat = new SimpleDateFormat("yyyy.MM.dd");
                result = transFormat.format(date);
                return result;
            case RESERVATION_TIME:
                transFormat = new SimpleDateFormat("yyyy.MM.dd aa hh:mm");
                result = transFormat.format(date);
                return result;
            case RESERVATION_DETAIL:
                transFormat = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm");
                result = transFormat.format(date);
                return result;
            default:
                break;
        }
        return result;
    }
}
