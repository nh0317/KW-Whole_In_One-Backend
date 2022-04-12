package com.naturemobility.seoul.service.calculateManagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculationListRes;

import java.util.List;

public interface CalculateManagementService {
    public List<GetCalculationListRes> getCalculateList(Long partnerIdx) throws BaseException;

    public List<GetCalculationListRes> getCalculateListWithFilter(
            Long partnerIdx, String startDate, String endDate, Integer [] calculationStatus) throws BaseException;

    public void postCalculation(Long partnerIdx,Long partnerPaymentIdx) throws BaseException;

    //public void calculate(Long partnerIdx, String calculationMonthDate) throws BaseException;
}
