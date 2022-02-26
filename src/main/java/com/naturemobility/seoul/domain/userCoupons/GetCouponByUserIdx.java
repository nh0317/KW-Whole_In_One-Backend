package com.naturemobility.seoul.domain.userCoupons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetCouponByUserIdx {
    private Long couponIdx;
    private String couponName;
    private int amount;
    private Boolean isUsable;
}
