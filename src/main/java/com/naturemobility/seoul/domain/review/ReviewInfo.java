package com.naturemobility.seoul.domain.review;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewInfo extends DTOCommon {

    // 리뷰 인덱스
    private Long reviewIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 유저 인덱스
    private Long userIdx;

    // 리뷰 내용
    private String review;

    // 리뷰 점수
    private Float reviewScore;

    private Long reservationIdx;

    public ReviewInfo(Long storeIdx, Long userIdx, Float reviewScore, Long reservationIdx) {
        this.storeIdx = storeIdx;
        this.userIdx = userIdx;
        this.reviewScore = reviewScore;
        this.reservationIdx = reservationIdx;
    }
}