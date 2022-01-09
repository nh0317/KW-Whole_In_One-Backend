package com.naturemobility.seoul.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PriceSchemeInfo {
    // 가격 스키마 인텍스
    private Long priceSchemeIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 가격 스키마 이름
    private String name;

    // 수정한 사람
    private Long modiBy;

    private Date deletedAt;

    public PriceSchemeInfo(Long storeIdx, String name, Long modiBy) {
        this.storeIdx = storeIdx;
        this.name = name;
        this.modiBy = modiBy;
    }
}
