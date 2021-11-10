package Nature_Mobility.Whole_In_One.Backend.domain.coupons;


import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
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