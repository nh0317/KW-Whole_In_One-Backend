package com.naturemobility.seoul.service.calculateManagement;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;
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
    public List<GetCalculateListRes> getCalculateList(Long partnerIdx, String startDay, String endDay) throws BaseException {
        List<GetCalculateListRes> calculationList = null;
        Long storeIdx = calculateManagementMapper.getStoreIdx(partnerIdx);
        calculationList = calculateManagementMapper.getCalculateList(storeIdx, startDay, endDay);
        return calculationList;
    }
}
