package com.naturemobility.seoul.domain;

import com.naturemobility.seoul.domain.paging.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DTOCommon extends PageInfo {
    public DTOCommon(int page, int total) {
        super(page,total);
    }
    public DTOCommon(){
        super();
    }
    //원래 DB Table 타입은 Timestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
