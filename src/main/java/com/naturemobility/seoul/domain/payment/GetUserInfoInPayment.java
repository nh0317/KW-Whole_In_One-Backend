package com.naturemobility.seoul.domain.payment;

import com.naturemobility.seoul.domain.userCoupons.GetCouponByUserIdx;
import com.naturemobility.seoul.domain.users.GetUserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetUserInfoInPayment {
    private String phoneNum;
    private String userName;
    private String userEmail;
    private Integer userPoint;
    private int userCoupon;
    private List<GetUserCoupon> userCoupons;

    public GetUserInfoInPayment(String phoneNum, String userName, String userEmail, Integer userPoint) {
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPoint = userPoint;
    }
}
