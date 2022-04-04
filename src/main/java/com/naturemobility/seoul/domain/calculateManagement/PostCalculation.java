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
    // 정산 금액
    private Integer price;
    //정산 요청 월
    private String calculationMonthDate;

    public PostCalculation(Long partnerIdx, Integer sales, String calculationMonthDate) {
        this.partnerIdx = partnerIdx;
        this.sales = sales;
        this.fee = (int) Math.round(this.sales * DEFAULTFEE * 0.01);
        this.price = sales - (int) Math.round(this.sales * DEFAULTFEE * 0.01);
        this.calculationMonthDate = calculationMonthDate;
    }
}