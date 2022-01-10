package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.dashboard.*;
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
     * [GET] /today-sales
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
     * 오늘의 예약 현황 조회
     * [GET] /reservations
     * @return BaseResponse<List<GetRezListRes>>
     */
    @GetMapping("/reservations")
    public BaseResponse<List<GetRezListRes>> getTodayReservations() {
        Long partnerIdx=0L;
        try {
            partnerIdx = checkUserService.getPartnerIdx();
            List<GetRezListRes> todayRezList = dashBoardService.getTodayRezList(partnerIdx);
            return new BaseResponse<>(SUCCESS,todayRezList);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 월 매출, 예약 현황 조회
     * [GET] /month-sales
     * @return BaseResponse<GetMonthRes>
     */
    @GetMapping("/month-sales")
    public BaseResponse<GetMonthRes> getMonthSales() {
        Long partnerIdx=0L;
        try {
            partnerIdx = checkUserService.getPartnerIdx();
            GetMonthRes monthRes = dashBoardService.getMonthRes(partnerIdx);
            return new BaseResponse<>(SUCCESS,monthRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 캘린더 데이터 가져오기
     * [GET] /calendar
     * @return BaseResponse<List<GetCalendarRes>>
     */
    @GetMapping("/calendar")
    public BaseResponse<List<GetCalendarRes>> getCalendar() {
        Long partnerIdx=0L;
        try {
            partnerIdx = checkUserService.getPartnerIdx();
            List<GetCalendarRes> calendarRes = dashBoardService.getCalendarList(partnerIdx);
            return new BaseResponse<>(SUCCESS,calendarRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
