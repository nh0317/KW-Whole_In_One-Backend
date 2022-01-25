package com.naturemobility.seoul.domain.dashboard;

import lombok.Getter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)

@Getter
public class PostMemoInfo {
    private String memo;
    private Long partnerIdx;

    public PostMemoInfo(String memo,Long partnerIdx){
        this.memo = memo;
        this.partnerIdx = partnerIdx;
    }
}
