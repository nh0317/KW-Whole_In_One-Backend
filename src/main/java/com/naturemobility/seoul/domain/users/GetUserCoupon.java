package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserCoupon {
    private Long couponIdx;
    private String storeName;
    private String couponDeadline;
    private String couponName;
    private Integer couponPercentage;
    private Integer couponStatus;
}