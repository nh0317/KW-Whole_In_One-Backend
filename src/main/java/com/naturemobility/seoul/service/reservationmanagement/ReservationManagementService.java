package com.naturemobility.seoul.service.reservationmanagement;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezPerMonth;

import java.util.List;

public interface ReservationManagementService {
    public List<GetRezListByManagementRes> getRezList(Long partnerIdx,String reservationDay) throws BaseException;
    public void deleteRez(Long partnerIdx, Long reservationIdx) throws BaseException;

    List<GetRezPerMonth> getRezListPerMonth(Long partnerIdx, String date);
}
