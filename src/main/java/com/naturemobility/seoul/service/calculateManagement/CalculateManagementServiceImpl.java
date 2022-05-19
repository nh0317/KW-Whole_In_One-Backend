package com.naturemobility.seoul.service.calculateManagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculationListRes;
import com.naturemobility.seoul.domain.calculateManagement.PostCalculation;
import com.naturemobility.seoul.mapper.CalculateManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CalculateManagementServiceImpl implements CalculateManagementService {
    @Autowired
    CalculateManagementMapper calculateManagementMapper;

    @Override
    public List<GetCalculationListRes> getCalculateList(Long partnerIdx) throws BaseException {
        //Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        postPartnerPayment(partnerIdx);
        List<GetCalculationListRes> calculationList = calculateManagementMapper.getCalculateList(partnerIdx);
        return calculationList;
    }

    @Override
    public List<GetCalculationListRes> getCalculateListWithFilter(
            Long partnerIdx, String startDate, String endDate, Integer [] calculationStatus) throws BaseException {

        postPartnerPayment(partnerIdx);
        List<GetCalculationListRes> calculationList = calculateManagementMapper.getCalculateListWithFilter(
                partnerIdx, startDate, endDate, calculationStatus);
        return  calculationList;
    }

    private void postPartnerPayment(Long partnerIdx) {
        //현재 날짜를 기준으로 한달전을 string 값으로 가져옴
        String getCheckMonthRes = "2022-01";
        //전달에 정산된 기록 체크
        int duplicationVerification = calculateManagementMapper.checkDuplication(partnerIdx, getCheckMonthRes);

        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        int amount = calculateManagementMapper.getCalculatedAmount(storeIdx, getCheckMonthRes);
        //정산된 기록이 없다면 전달 정산을 함
        if (duplicationVerification == 0 && amount != 0) {
//            int canceledAmount = calculateManagementMapper.getCanceledAmount(storeIdx, getCheckMonthRes);
            int couponDiscount = calculateManagementMapper.getCouponDiscount(storeIdx, getCheckMonthRes);
            int pointDiscount = calculateManagementMapper.getDiscountPoint(storeIdx, getCheckMonthRes);
//            amount -= canceledAmount;
            PostCalculation postCalculation = new PostCalculation(partnerIdx, amount, couponDiscount, pointDiscount, getCheckMonthRes);
            calculateManagementMapper.postCalculation(postCalculation);

        }
    }

    @Override
    public void postCalculation(Long partnerIdx, Long partnerPaymentIdx) throws BaseException {

        Long partnerIdxFromPartnerPaymentIdx = calculateManagementMapper.getPartnerIdxFromPartnerPaymentIdx(partnerPaymentIdx);
        if(partnerIdx != partnerIdxFromPartnerPaymentIdx){
            throw new BaseException(BaseResponseStatus.NO_AUTHORITY);
        }

        int checkCalculationDuplication = calculateManagementMapper.checkCalculationDuplication(partnerPaymentIdx);
        if(checkCalculationDuplication == 1){
            throw new BaseException(BaseResponseStatus.DUPLICATION_CALCULATION);
        }

        calculateManagementMapper.changeCalculateStatus(partnerPaymentIdx);
        return;
    }

    /*
    @Override
    public void calculate(Long partnerIdx, Long calculationIdx) throws BaseException {
        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        Long storeIdxByCalculationIdx = calculateManagementMapper.getStoreIdxByCalculationIdx(calculationIdx);
        if (storeIdx != storeIdxByCalculationIdx) {
            throw new BaseException(NO_AUTHORITY);
        } else {
            calculateManagementMapper.changeCalculateStatus(calculationIdx);
        }
    }
     */

    /* 2022-04-12 코드 수정
    @Override
    public void calculate(Long partnerIdx, String calculationMonthDate) throws BaseException {

        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        int amount = calculateManagementMapper.getCalculatedAmount(storeIdx, calculationMonthDate);
        int canceledAmount = calculateManagementMapper.getCanceledAmount(storeIdx, calculationMonthDate);
        amount -= canceledAmount;

        int duplicationVerification = calculateManagementMapper.checkDuplication(partnerIdx, calculationMonthDate);
        if (duplicationVerification == 1) {
            throw new BaseException(DUPLICATION_CALCULATION);
        } else {
            PostCalculation postCalculation = new PostCalculation(partnerIdx, amount, calculationMonthDate);
            calculateManagementMapper.postCalculation(postCalculation);
            return;
        }
    }
     */
}
