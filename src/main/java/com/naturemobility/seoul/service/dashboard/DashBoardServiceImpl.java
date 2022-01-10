package com.naturemobility.seoul.service.dashboard;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.dashboard.GetTodayRes;
import com.naturemobility.seoul.mapper.DashBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    DashBoardMapper dashBoardMapper;

    @Override
    public GetTodayRes getTodayRes(Long partnerIdx) throws BaseException {
        GetTodayRes todayRes;
        try {
            Long storeIdx = dashBoardMapper.getStoreIdx(partnerIdx);
            todayRes = dashBoardMapper.getTodayRes(storeIdx);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return todayRes;
    }
}
