package com.naturemobility.seoul.service.visited;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.VisitedMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VisitedServiceImpl implements VisitedService {
    @Autowired
    VisitedMapper visitedMapper;
    @Autowired
    StoresMapper storesMapper;


    /**
     * 방문한 스토어 표시
     * @param storeIdx
     * @param userIdx
     * @throws BaseException
     */
    @Override
    public void setVistiedStore(Long storeIdx, Long userIdx) throws BaseException {
        VisitedInfo visitedInfo = new VisitedInfo(storeIdx,userIdx);
        if (userIdx==0) //로그인 하지 않은 경우 예외 처리
            return;

        if(visitedMapper.findByStoreIdxAndUserIdx(visitedInfo).isPresent())
            visitedMapper.update(visitedInfo);
        else
            visitedMapper.save(visitedInfo);
    }

    /**
     * 방문한 스토어 목록 조회
     * @param userIdx
     * @return List<VisitedInfo>
     * @throws BaseException
     */
    @Override
    public List<GetVisitedByUserIdx> findAllVisitedStore(Long userIdx, Integer page) throws BaseException {
            VisitedInfo visitedInfo = new VisitedInfo(userIdx);
            List<GetVisitedByUserIdx> result = new ArrayList<>();

            int totalVisited = visitedMapper.cntTotalVisited(visitedInfo.getUserIdx());
            VisitedInfo pagingInfo = (VisitedInfo) visitedInfo.getPageInfo(page, totalVisited);

            if (pagingInfo != null) {
                result = visitedMapper.findAllByUserIdx(pagingInfo);
            }
            return result;
    }

    @Override
    public Map<String,Integer> getTotalPage(Long userIdx){
        VisitedInfo visitedInfo = new VisitedInfo(userIdx);
        int totalVisited = visitedMapper.cntTotalVisited(visitedInfo.getUserIdx());
        visitedInfo.setTotalData(totalVisited);

        Map<String, Integer> result = new HashMap<>();
        result.put("totalPage", visitedInfo.getTotalPage());
        return result;
    }
}
