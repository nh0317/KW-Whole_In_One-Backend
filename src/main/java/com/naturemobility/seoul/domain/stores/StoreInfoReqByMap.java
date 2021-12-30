package com.naturemobility.seoul.domain.stores;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)

// 골프장 지도 요청 정보
public class StoreInfoReqByMap {

    Double userLatitude;
    Double userLongitude;
    Integer orderRule;
    Integer brand;
    Integer lefthandStatus;
    Integer parkingStatus;
    Integer groupseatStatus;
    Integer floorscreenStatus;
    Integer storageStatus;
    Integer lessonStatus;
    Integer distance;
}
