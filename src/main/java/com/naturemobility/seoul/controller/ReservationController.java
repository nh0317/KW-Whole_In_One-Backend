package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.domain.reservations.GetRezTime;
import com.naturemobility.seoul.domain.reservations.PostRezReq;
import com.naturemobility.seoul.domain.stores.GetBrandRes;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.REQUEST_ERROR;
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
            GetRezRes reservation = reservationsService.findByRezIdx(reservationIdx);
            return new BaseResponse<>(SUCCESS, reservation);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 예약 하기
     * [post] /reservation
     * Body
     * {
     *     "storeIdx":1,
     *     "reservationTime":"2022-01-12 14:00",
     *     "useTime":30,
     *     "numberOfGame":4,
     *     "selectedHall":2,
     *     "request":"hello",
     *     "personCount":4,
     *     "price":3000,
     *     "discountPrice":1000
     * }
     * @return
     */
    @PostMapping("")
    public BaseResponse postReservation(@RequestBody PostRezReq postRezReq) {
        Long userIdx=0L;
        try {
            userIdx = checkUserService.getUserIdx();
            reservationsService.postReservation(postRezReq,userIdx);
            return new BaseResponse(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse(exception.getStatus());
        }
    }

    /**
     * 예약 시간 조회
     * [post] /reservation
     * @return BaseResponse<GetRezTime>
     */
    @ResponseBody
    @GetMapping("/reserved-time")
    public BaseResponse<List<GetRezTime>> getReservationTime(@RequestParam("storeIdx") Long storeIdx,@RequestParam("reservationDay") String reservationDay) {
        try {
            List<GetRezTime> getRezTime = reservationsService.getReservationTime(storeIdx,reservationDay);
            return new BaseResponse<>(SUCCESS,getRezTime);
        } catch (BaseException exception) {
            return new BaseResponse(exception.getStatus());
        }
    }
}
