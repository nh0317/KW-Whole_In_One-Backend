package com.naturemobility.seoul.domain.visited;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitedInfo extends DTOCommon {
    // 관심 매장 인덱스
    private Long visitedIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 유저 인덱스
    private Long userIdx;

    public VisitedInfo(){};
    public VisitedInfo(Long storeIdx, Long userIdx) {
        this.storeIdx = storeIdx;
        this.userIdx = userIdx;
    }
    public VisitedInfo(Long userIdx) {
        this.userIdx = userIdx;
    }
}
