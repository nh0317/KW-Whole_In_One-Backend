package com.naturemobility.seoul.service.calculateManagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
import com.naturemobility.seoul.domain.calculateManagement.PostCalculation;
import com.naturemobility.seoul.mapper.CalculateManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.DUPLICATION_CALCULATION;
import static com.naturemobility.seoul.config.BaseResponseStatus.NO_AUTHORITY;

@Service
@Slf4j
public class CalculateManagementServiceImpl implements CalculateManagementService {
    @Autowired
    CalculateManagementMapper calculateManagementMapper;

    @Override
    public List<GetCalculateListRes> getCalculateList(Long partnerIdx, String startDay, String endDay) throws BaseException {
        List<GetCalculateListRes> calculationList = null;
        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        calculationList = calculateManagementMapper.getCalculateList(storeIdx, startDay, endDay);
        return calculationList;
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

    @Override
    public void calculate(Long partnerIdx, String calculationMonthDate) throws BaseException {

        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        int amount = calculateManagementMapper.getCalculatedAmount(storeIdx, calculationMonthDate);

        int duplicationVerification = calculateManagementMapper.checkDuplication(partnerIdx, calculationMonthDate);
        if (duplicationVerification == 1) {
            throw new BaseException(DUPLICATION_CALCULATION);
        } else {
            PostCalculation postCalculation = new PostCalculation(partnerIdx, amount, calculationMonthDate);
            calculateManagementMapper.postCalculation(postCalculation);
            return;
        }
    }
}
