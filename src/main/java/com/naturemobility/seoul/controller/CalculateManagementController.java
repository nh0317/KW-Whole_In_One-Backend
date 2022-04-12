package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculationListRes;
import com.naturemobility.seoul.service.calculateManagement.CalculateManagementService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("calculate-management")
public class CalculateManagementController {

    @Autowired
    private CalculateManagementService calculateManagementService;

    @Autowired
    CheckUserService checkUserService;

    /*
    /**
     * 정산관리 내역 조회
     * [GET] /calculate-management/calculation-list?startDay=2021-11-02&endDay=2021-12-10
     *
     * @return BaseResponse<List < GetCalculateListRes>>
     */
    /*
    @GetMapping("/calculation-list")
    public BaseResponse<List<GetCalculateListRes>> getCalculationList(@RequestParam("startDay") String startDay,
                                                                      @RequestParam("endDay") String endDay) throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetCalculateListRes> calculateList = calculateManagementService.getCalculateList(partnerIdx, startDay, endDay);
        return new BaseResponse<>(SUCCESS, calculateList);
    }
    */

    /*
    /**
     * 정산하기
     * [PATCH] /calculate-management/calculate?calculationIdx=11
     * @return BaseResponse
     */
    /*
    @PatchMapping("/calculate")
    public BaseResponse calculate(@RequestParam("calculationIdx") Long calculationIdx) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        calculateManagementService.calculate(partnerIdx,calculationIdx);
        return new BaseResponse<>(SUCCESS);
    }
     */

    /*
    /**
     * 정산하기
     * [POST] /calculate-management/calculation?calculationMonthDate= 2022-02
     *
     * @return BaseResponse
     */
    /*
    2022-04-12 정산하기 부분 코드를 월말에 정산 내역조회 시 DB를 확인 후 따라서 자동으로 정산하기가 되도록 변경
    @PostMapping("/calculation")
    public BaseResponse calculate(@RequestParam("calculationMonthDate") String calculationMonthDate) throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = checkUserService.getPartnerIdx();
        calculateManagementService.calculate(partnerIdx, calculationMonthDate);
        return new BaseResponse<>(SUCCESS);
    }*/

    /**
     * 정산관리 내역 조회
     * [GET] /calculate-management/calculation
     *
     * @return BaseResponse<List < GetCalculateListRes>>
     */
    @GetMapping("/calculation")
    public BaseResponse<List<GetCalculationListRes>> getCalculationList() throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetCalculationListRes> calculateList = calculateManagementService.getCalculateList(partnerIdx);
        return new BaseResponse<>(SUCCESS, calculateList);
    }

    /**
     * 정산관리 내역 조회
     * [GET] /calculate-management/calculation?startDate=&endDate=&calculationStatus
     *
     * @return BaseResponse<List < GetCalculateListRes>>
     */
    @GetMapping("/calculation-list")
    public BaseResponse<List<GetCalculationListRes>> getCalculationListWithFilter(
            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
            @RequestParam("calculationStatus") Integer calculationStatus) throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetCalculationListRes> calculateList = calculateManagementService.getCalculateListWithFilter(
                partnerIdx, startDate, endDate, calculationStatus);
        return new BaseResponse<>(SUCCESS, calculateList);
    }

    /**
     * 정산요청
     * [POST] /calculate-management/calculation?partnerPaymentIdx=1
     *
     * @return BaseResponse<List < GetCalculateListRes>>
     */
    @PostMapping("/calculation")
    public BaseResponse postCalculation(
            @RequestParam("partnerPaymentIdx") Long partnerPaymentIdx) throws BaseException {
        Long partnerIdx = 0L;
        partnerIdx = checkUserService.getPartnerIdx();
        calculateManagementService.postCalculation(partnerIdx, partnerPaymentIdx);
        return new BaseResponse<>(SUCCESS);
    }
}
