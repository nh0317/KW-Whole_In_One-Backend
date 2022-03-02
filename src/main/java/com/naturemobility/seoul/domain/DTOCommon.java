package com.naturemobility.seoul.domain;

import com.naturemobility.seoul.domain.paging.Criteria;
import com.naturemobility.seoul.domain.paging.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class DTOCommon extends Criteria {
    private PageInfo PageInfo;
    //원래 DB Table 타입은 Timestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
