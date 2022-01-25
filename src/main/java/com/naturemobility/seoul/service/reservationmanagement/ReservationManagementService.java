package com.naturemobility.seoul.service.reservationmanagement;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;

import java.util.List;

public interface ReservationManagementService {
    public List<GetRezListByManagementRes> getRezList(Long partnerIdx,String reservationDay) throws BaseException;
}
