package com.naturemobility.seoul.domain.coupons;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponInfo {

    // 쿠폰 인덱스
    private Long couponIdx;

    // 쿠폰 이름
    private String couponName;

    // 할인 퍼센트
    private Integer couponPercentage;

    // 쿠폰 기한
    private String couponDeadline;

}