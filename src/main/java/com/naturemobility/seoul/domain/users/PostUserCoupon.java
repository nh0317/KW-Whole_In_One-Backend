package com.naturemobility.seoul.domain.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostUserCoupon {

    private Long couponIdx;
    private Long userIdx;

    public PostUserCoupon(Long couponIdx, Long userIdx) {
        this.couponIdx = couponIdx;
        this.userIdx = userIdx;
    }
}