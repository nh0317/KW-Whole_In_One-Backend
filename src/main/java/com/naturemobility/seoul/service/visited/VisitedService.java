package com.naturemobility.seoul.service.visited;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;

import java.util.List;
import java.util.Map;

public interface VisitedService {
    //방문한 스토어를 목록에 넣는 함수
    void setVistiedStore(Long storeIdx, Long userIdx) throws BaseException;

    //유저가 방문한 스토어의 목록을 반환하는 함수
    List<GetVisitedByUserIdx> findAllVisitedStore(Long userIdx, Integer page) throws BaseException;

    Map<String,Integer> getTotalPage(Long userIdx);
}
