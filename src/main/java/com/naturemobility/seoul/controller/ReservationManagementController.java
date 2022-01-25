package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.reservationmanagement.GetRezListByManagementRes;
import com.naturemobility.seoul.service.reservationmanagement.ReservationManagementService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("reservation-management")
public class ReservationManagementController {

    @Autowired
    private ReservationManagementService reservationManagementService;

    @Autowired
    CheckUserService checkUserService;

    /**
     * 예약 현황 조회
     * [GET] /reservation-management/reservations/day?reservationDay=2022-01-10
     * @return BaseResponse<List<GetRezListByManagementRes>>
     */
    @GetMapping("/reservations/day")
    public BaseResponse<List<GetRezListByManagementRes>> getReservations(@RequestParam("reservationDay") String reservationDay) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetRezListByManagementRes> rezList = reservationManagementService.getRezList(partnerIdx,reservationDay);
        return new BaseResponse<>(SUCCESS,rezList);
    }
}
