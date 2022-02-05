package com.naturemobility.seoul.service.calculateManagement;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.calculateManagement.GetCalculateListRes;

import java.util.List;

public interface CalculateManagementService {
    public List<GetCalculateListRes> getCalculateList(Long partnerIdx, String startDay, String endDay) throws BaseException;
}
