package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserCoupon {
    private final Long couponIdx;
    private final String couponName;
    private final Integer couponPercentage;
    private final Integer couponStatus;
}