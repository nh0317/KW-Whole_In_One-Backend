package com.naturemobility.seoul.service.reservationmanagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import com.naturemobility.seoul.mapper.ReservationManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class ReservationManagementServiceImpl implements ReservationManagementService {
    @Autowired
    ReservationManagementMapper reservationManagementMapper;

    @Override
    public List<GetRezListByManagementRes> getRezList(Long partnerIdx, String reservationDay) throws BaseException {
        List<GetRezListByManagementRes> rezList;
        Long storeIdx = reservationManagementMapper.getStoreIdx(partnerIdx);
        rezList = reservationManagementMapper.getRezList(reservationDay,storeIdx);
        return rezList;
    }

    @Override
    public void deleteRez(Long partnerIdx, Long reservationIdx) throws BaseException {
        Long storeIdx = reservationManagementMapper.getStoreIdx(partnerIdx);
        Long storeIdxByRezIdx = reservationManagementMapper.getStoreIdxByRezIdx(reservationIdx);
        if (storeIdx != storeIdxByRezIdx) {
            throw new BaseException(NO_AUTHORITY);
        } else {
            reservationManagementMapper.deleteRez(reservationIdx);
        }
    }
}
