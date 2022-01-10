package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.dashboard.GetTodayRes;
import com.naturemobility.seoul.service.dashboard.DashBoardService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    CheckUserService checkUserService;
    /**
     * 오늘의 매출,예약 수 조회
     * [GET] /todaysales
     * @return BaseResponse<GetTodayRes>
     */
    @GetMapping("/today-sales")
    public BaseResponse<GetTodayRes> getTodaySales() {
        Long partnerIdx=0L;
        try {
            partnerIdx = checkUserService.getPartnerIdx();
            GetTodayRes todayRes = dashBoardService.getTodayRes(partnerIdx);
            return new BaseResponse<>(SUCCESS,todayRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 오늘의 매출,예약 수 조회
     * [GET] /todaysales
     * @return BaseResponse<GetTodayRes>
     */
    @GetMapping("/today-sales")
    public BaseResponse<GetTodayRes> getTodaySales() {
        Long partnerIdx=0L;
        try {
            partnerIdx = checkUserService.getPartnerIdx();
            GetTodayRes todayRes = dashBoardService.getTodayRes(partnerIdx);
            return new BaseResponse<>(SUCCESS,todayRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
