package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisitedMapper {
    int save(VisitedInfo visitedInfo);
    int delete(VisitedInfo visitedInfo);

    Integer cntTotalVisited(@Param("userIdx") Long userIdx);
    List<GetVisitedByUserIdx> findAllByUserIdx(VisitedInfo visitedInfo);
}
