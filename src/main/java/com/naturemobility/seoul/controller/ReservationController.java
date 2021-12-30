package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;


@RestController
@RequestMapping("reservation")
public class ReservationController {
    @Autowired
    ReservationsService reservationsService;
    @Autowired
    CheckUserService checkUserService;
    /**
     * 예약 목록 조회
     * [GET] /reservation
     * @return BaseResponse<List<GetRezResByUserIdx>>
     */
    @GetMapping("")
    public BaseResponse<List<GetRezResByUserIdx>> getReservationList(@RequestParam(value = "page",required = false) Integer page) {
        Long userIdx=0L;
        try {
            userIdx = checkUserService.getUserIdx();
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try {
            List<GetRezResByUserIdx> allReservations = reservationsService.findByUserIdx(userIdx, page);
            return new BaseResponse<>(SUCCESS, allReservations);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 예약 목록 조회
     * [GET] /reservation/{reservationIdx}
     * @return BaseResponse<GetRezRes>
     */
    @GetMapping("{reservationIdx}")
    public BaseResponse<GetRezRes> getReservation(@PathVariable("reservationIdx") Long reservationIdx) {
        try {
            Long userIdx = checkUserService.getUserIdx();
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try {
            GetRezRes reservation = reservationsService.findByRezIdx(reservationIdx);
            return new BaseResponse<>(SUCCESS, reservation);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
