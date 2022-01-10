package com.naturemobility.seoul.service.dashboard;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.dashboard.*;
import java.util.List;

public interface DashBoardService {
    public GetTodayRes getTodayRes(Long partnerIdx) throws BaseException;
}
