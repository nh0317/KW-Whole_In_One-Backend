package com.naturemobility.seoul.domain.stores;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
// 골프장 정보
public class StoreInfo extends DTOCommon {

    // 골프장 인덱스
    private Long storeIdx;

    // 골프장 이름
    private String storeName;

    // 매장소개
    private String storeInfo;

    // 골프장 전화번호
    private String storePhoneNumber;

    // 골프장 브랜드
    // TODO: 골프장 브랜드 수정
    private Long storeBrand;

    // 골프장 주소
    private String storeLocation;

    // 골프장 위도
    private Double storeLatitude;

    // 골프장 경도
    private Double storeLongitude;

    // 골프장 대표이미지
    private String storeImage;

    // 골프장 영업시간
    private String storeTime;

    // 타석 수
    private String batCount;

    // 왼손타석 여부 0: 없음 1:가능
    private Boolean leftHandsatus;

    // 주차시설 여부 0: 없음 1:가능
    private Boolean parkingStatus;

    // 단체석 여부 0: 없음 1:가능
    private Boolean groupSeatStatus;

    // 바닥 스크린 여부 0: 없음 1:가능
    private Boolean floorScreenStatus;

    // 장비 보관 시설 여부 0: 없음 1:가능
    private Boolean storageStatus;

    // 프로교습 여부 0: 없음 1:가능
    private Boolean lessonStatus;

    // 당일 예약 가능 여부 0: 불가능, 1: 가능
    private Boolean reserveStatus;

    // 쿠폰 사용 가능 여부 0: 불가능, 1: 가능
    private Boolean couponStatus;
}
