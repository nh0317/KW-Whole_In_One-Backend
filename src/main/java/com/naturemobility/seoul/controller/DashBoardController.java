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
    public BaseResponse<GetTodayRes> getTodaySales() throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        GetTodayRes todayRes = dashBoardService.getTodayRes(partnerIdx);
        return new BaseResponse<>(SUCCESS,todayRes);
    }

    /**
     * 오늘의 예약 현황 조회
     * [GET] /reservations
     * @return BaseResponse<List<GetRezListRes>>
     */
    @GetMapping("/reservations")
    public BaseResponse<List<GetRezListRes>> getTodayReservations() throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetRezListRes> todayRezList = dashBoardService.getTodayRezList(partnerIdx);
        return new BaseResponse<>(SUCCESS,todayRezList);
    }

    /**
     * 월 매출, 예약 현황 조회
     * [GET] /month-sales
     * @return BaseResponse<GetMonthRes>
     */
    @GetMapping("/month-sales")
    public BaseResponse<GetMonthRes> getMonthSales() throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        GetMonthRes monthRes = dashBoardService.getMonthRes(partnerIdx);
        return new BaseResponse<>(SUCCESS,monthRes);
    }

    /**
     * 캘린더 데이터 가져오기
     * [GET] /calendar
     * @return BaseResponse<List<GetCalendarRes>>
     */
    @GetMapping("/calendar")
    public BaseResponse<List<GetCalendarRes>> getCalendar() throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetCalendarRes> calendarRes = dashBoardService.getCalendarList(partnerIdx);
        return new BaseResponse<>(SUCCESS,calendarRes);
    }

    /**
     * 특정 날짜 예약 현황 조회
     * [GET] /reservations/day
     * @return BaseResponse<List<GetRezListRes>>
     */
    @GetMapping("/reservations/day")
    public BaseResponse<List<GetRezListRes>> getSpecificReservations(@RequestParam("reservationDay") String reservationDay) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetRezListRes> specificRezList = dashBoardService.getSpecificRezList(partnerIdx,reservationDay);
        return new BaseResponse<>(SUCCESS,specificRezList);
    }

    /**
     * 관리메모 조회
     * [GET] /dashboard/memo
     * @return BaseResponse<List<GetMemoRes>>
     */
    @GetMapping("/memo")
    public BaseResponse<List<GetMemoRes>> getMemo() throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetMemoRes> memoList = dashBoardService.getMemoList(partnerIdx);
        return new BaseResponse<>(SUCCESS,memoList);
    }

    /**
     * 관리메모 생성
     * [POST] /dashboard/memo
     * body{
     * content : 안녕
     * }
     * @return
     */
    @PostMapping("/memo")
    public BaseResponse postMemo(@RequestBody PostMemo memo) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        dashBoardService.postMemo(memo,partnerIdx);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 관리메모 삭제
     * [DELETE] /dashboard/{memoIdx}
     * @return
     */
    @DeleteMapping("/memo/{memoIdx}")
    public BaseResponse deleteMemo(@PathVariable("memoIdx") Long memoIdx) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        dashBoardService.deleteMemo(memoIdx,partnerIdx);
        return new BaseResponse<>(SUCCESS);
    }
}
