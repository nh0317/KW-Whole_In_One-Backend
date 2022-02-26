package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.reservations.*;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public BaseResponse<List<GetRezResByUserIdx>> getReservationList(@RequestParam(value = "page",required = false) Integer page) throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
            List<GetRezResByUserIdx> allReservations = reservationsService.findByUserIdx(userIdx, page);
            return new BaseResponse<>(SUCCESS, allReservations);
    }

    /**
     * 예약 목록 조회
     * [GET] /reservation/{reservationIdx}
     * @return BaseResponse<GetRezRes>
     */
    @GetMapping("{reservationIdx}")
    public BaseResponse<GetRezRes> getReservation(@PathVariable("reservationIdx") Long reservationIdx) throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        GetRezRes reservation = reservationsService.findByRezIdx(reservationIdx);
        return new BaseResponse<>(SUCCESS, reservation);
    }

    /**
     * 예약 하기
     * [post] /reservation
     * Body
     * {
     *     "storeIdx":1,
     *     "reservationTime":"2022-01-12 14:00",
     *     "useTime":30,
     *     "endTime":"2022-01-12 14:30"
     *     "numberOfGame":4,
     *     "roomIdx":3
     *     "selectedHall":2,
     *     "request":"hello",
     *     "personCount":4,
     *     "price":3000,
     *     "discountPrice":1000
     * }
     * @return
     */
    @PostMapping("")
    public BaseResponse postReservation(@RequestBody PostRezReq postRezReq) throws BaseException {
        Long userIdx= checkUserService.getUserIdx();
        reservationsService.postReservation(postRezReq,userIdx);
        return new BaseResponse(SUCCESS);
    }

    /**
     * 예약 시간 조회
     * [GET] /reservation-time?storeIdx=1&reservationDay="2021-01-09"&hallNumber=2
     * @return BaseResponse<List<GetRezTime>>
     */
    @ResponseBody
    @GetMapping("/reserved-time")
    public BaseResponse<List<GetRezTime>> getReservationTime(@RequestParam("storeIdx") Long storeIdx,@RequestParam("reservationDay") String reservationDay,
                                                             @RequestParam("roomIdx") Long roomIdx) throws BaseException{
        List<GetRezTime> getRezTime = reservationsService.getReservationTime(storeIdx,reservationDay,roomIdx);
        return new BaseResponse<>(SUCCESS,getRezTime);
    }

    @GetMapping("total_page")
    public BaseResponse<Map<String,Integer>> getTotalPage() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        Map<String, Integer> result = reservationsService.getTotalPage(userIdx);
        return new BaseResponse<>(SUCCESS, result);
    }

    @GetMapping("/get_rez_store_info/{storeIdx}")
    public BaseResponse<GetRezResByStoreIdx> getStoreInfo(@PathVariable("storeIdx")Long storeIdx) throws BaseException{
        GetRezResByStoreIdx result = reservationsService.getStoreInfo(storeIdx);
        return new BaseResponse<>(SUCCESS, result);
    }
}
