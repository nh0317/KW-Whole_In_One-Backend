package Nature_Mobility.Whole_In_One.Backend.domain.userCoupons;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 유저가 가지고 있는 쿠폰
public class userCouponInfo extends DTOCommon {

    // 유저인덱스
    private Long userIdx;

    // 쿠폰 인덱스
    private Long couponName;

    // 쿠폰 상태 0:미사용, 1:사용
    private Boolean couponStatus;

}
