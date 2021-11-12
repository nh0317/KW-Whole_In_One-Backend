package com.naturemobility.seoul.domain.partnerPayment;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class PartnerPaymentInfo extends DTOCommon {

    // 파트너 인덱스
    private Long partnerIdx;

    // 매출
    private Long sales;

    // 쿠폰 부담금액
    private Integer couponprice;

    // 수수료
    private Integer fees;

    // 정산 금액
    private Long price;

    // 정산여부 0:미완료, 1:완료
    private Boolean calculatestatus;

    // 정산일
    private Date calculatedate;

}