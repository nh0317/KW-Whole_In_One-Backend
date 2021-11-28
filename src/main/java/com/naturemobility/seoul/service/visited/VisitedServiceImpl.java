package com.naturemobility.seoul.service.visited;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.VisitedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
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
        VisitedInfo visitedInfo = new VisitedInfo(storeIdx, userIdx);
        if (userIdx==0) //로그인 하지 않은 경우 예외 처리
            return;

        try{
            visitedMapper.save(visitedInfo);
        }catch (Exception exception){
            throw new BaseException(NOT_FOUND_DATA);
        }
    }

    /**
     * 방문한 스토어 목록 조회
     * @param userIdx
     * @return List<VisitedInfo>
     * @throws BaseException
     */
    @Override
    public List<GetVisitedByUserIdx> findAllVistiedStore(Long userIdx) throws BaseException {
        VisitedInfo visitedInfo = new VisitedInfo(userIdx);
        List<GetVisitedByUserIdx> visiteds = new ArrayList<>();
        Integer totalVisited = visitedMapper.cntTotalVisited(visitedInfo.getUserIdx());
        if(totalVisited != null && totalVisited >0) {
            try {
                PageInfo pageInfo = new PageInfo(visitedInfo);
                pageInfo.setTotalPage(totalVisited);
                visitedInfo.setPageInfo(pageInfo);

                visiteds = visitedMapper.findAllByUserIdx(visitedInfo);
            } catch (Exception exception) {
                throw new BaseException(RESPONSE_ERROR);
            }
        } else throw new BaseException(NOT_FOUND_DATA);
        return visiteds;
    }
}
