package com.naturemobility.seoul.domain.storeImage;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 매장 이미지 파일
public class StoreImageFileInfo extends DTOCommon {

    // 이미지 인덱스
    private Long imgfileIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 골프장 이미지
    private String storeimage;

}