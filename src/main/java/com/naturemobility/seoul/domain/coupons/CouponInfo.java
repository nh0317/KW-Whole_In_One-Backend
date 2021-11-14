package com.naturemobility.seoul.domain.coupons;


import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class CouponInfo extends DTOCommon {

    // 쿠폰 인덱스
    private Long couponidx;

    // 쿠폰 이름
    private String couponname;

    // 할인 퍼센트
    private Boolean couponpercentage;

    // 쿠폰 기한
    private Date coupondeadline;

}