package com.naturemobility.seoul.domain.coupons;

import lombok.Getter;

@Getter
public class PostCouponReq {
   private String couponName;
   private Integer couponPercentage;
   private String couponDeadline;
}
