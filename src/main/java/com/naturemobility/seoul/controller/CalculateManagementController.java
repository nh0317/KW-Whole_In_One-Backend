package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
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

    /**
     * 정산관리 내역 조회
     * [GET] /calculate-management/calculation-list?startDay=2021-11-02&endDay=2021-12-10
     * @return BaseResponse<List<GetCalculateListRes>>
     */
    @GetMapping("/calculation-list")
    public BaseResponse<List<GetCalculateListRes>> getCalculationList(@RequestParam("startDay") String startDay,
                                                                      @RequestParam("endDay") String endDay) throws BaseException {
        Long partnerIdx=0L;
        partnerIdx = checkUserService.getPartnerIdx();
        List<GetCalculateListRes> calculateList = calculateManagementService.getCalculateList(partnerIdx,startDay,endDay);
        return new BaseResponse<>(SUCCESS,calculateList);
    }
}
