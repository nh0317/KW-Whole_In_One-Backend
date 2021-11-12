package com.naturemobility.seoul.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class DTOCommon {
    //원래 DB Table 타입은 Timestamp
    private Date createdat;
    private Date updatedat;
}
