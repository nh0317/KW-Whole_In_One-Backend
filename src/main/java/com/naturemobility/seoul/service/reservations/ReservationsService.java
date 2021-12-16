package com.naturemobility.seoul.service.reservations;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;

import java.util.List;

public interface ReservationsService {
    public GetRezRes findByRezIdx(Long reservationIdx) throws BaseException;
    public List<GetRezResByUserIdx> findByUserIdx(Long userIdx, Integer page) throws BaseException;
}
