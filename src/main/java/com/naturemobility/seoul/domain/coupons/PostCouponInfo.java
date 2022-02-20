package com.naturemobility.seoul.domain.coupons;


import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;


@Setter
@Getter
public class PostCouponInfo extends DTOCommon {

    //  매장 인덱스
    private Long storeIdx;
    // 쿠폰 이름
    private String couponName;
    // 할인 퍼센트
    private Integer couponPercentage;
    // 쿠폰 기한
    private String couponDeadline;

    public PostCouponInfo (Long storeIdx,String couponName,Integer couponPercentage,String couponDeadline) {
        this.storeIdx = storeIdx;
        this.couponName = couponName;
        this.couponPercentage = couponPercentage;
        this.couponDeadline = couponDeadline;
    }
}