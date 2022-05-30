package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface VisitedMapper {
    int save(VisitedInfo visitedInfo);
    int update(VisitedInfo visitedInf);
    int delete(VisitedInfo visitedInfo);


    int cntTotalVisited(@Param("userIdx") Long userIdx);
    List<GetVisitedByUserIdx> findAllByUserIdx(VisitedInfo visitedInfo);
    Optional<VisitedInfo> findByStoreIdxAndUserIdx(VisitedInfo visitedInfo);

}
