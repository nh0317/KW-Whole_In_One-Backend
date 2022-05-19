package com.naturemobility.seoul.domain.calculateManagement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PostCalculation {
    //TODO : 모든 매장의 수수료를 다르게 하고자 한다면 partner 테이블에 컬럼을 추가로 만들어서 가져오는 형식으로 수정이 필요할듯하다.
    final int DEFAULTFEE = 5;

    // 파트네 인덱스
    private Long partnerIdx;
    // 매출 금액
    private Integer sales;
    // 수수료 퍼센트
    private Integer fee;

    //쿠폰 할인 금액
    private int couponDiscount;

    //포인트 할인 금액
    private int pointDiscount;

    // 정산 금액
    private Integer price;
    //정산 요청 월
    private String getCheckMonthRes;

    public PostCalculation(Long partnerIdx, Integer sales,int couponDiscount, int pointDiscount, String getCheckMonthRes) {
        this.partnerIdx = partnerIdx;
        // 매출
        this.sales = sales;


        //할인 금액
        this.couponDiscount = couponDiscount;
        this.pointDiscount = pointDiscount;

        //매출에서 수수료 산정
        this.fee = (int) Math.round(this.sales * DEFAULTFEE * 0.01);

        // 할인 금액과 수수료를 제외한 정산 금액
        // 포인트는 플랫폼이 부담하므로 제외하지 않는다.
        this.price = sales - couponDiscount - fee;

        this.getCheckMonthRes = getCheckMonthRes;
    }
}