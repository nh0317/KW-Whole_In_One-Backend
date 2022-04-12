package com.naturemobility.seoul.service.reservationmanagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezPerMonth;
import com.naturemobility.seoul.mapper.ReservationManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<GetRezPerMonth> getRezListPerMonth(Long partnerIdx, String date) {
        Long storeIdx = reservationManagementMapper.getStoreIdx(partnerIdx);
        List<Integer> startDate = Arrays.stream(date.split("-")).map(Integer::parseInt).collect(Collectors.toList());

        Calendar cal = Calendar.getInstance();
        cal.set(startDate.get(0),startDate.get(1),1);
        String endDate = date+"-"+cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        date +="-01";
        return reservationManagementMapper.getRezListPerMonth(date,endDate,storeIdx);
    }
}
