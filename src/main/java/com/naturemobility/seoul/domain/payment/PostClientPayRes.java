package com.naturemobility.seoul.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostClientPayRes {
    String merchantUid; // 결제idx
    Integer amount;
    Integer earnPoint; // 적립포인트
    Integer usedPoint; // 사용한 포인트
    Integer point; //결제후 남은 포인트(적립금 포함)
    String status;

    public PostClientPayRes(String merchantUid, Integer amount, Integer earnPoint, Integer usedPoint, Integer point) {
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.earnPoint = earnPoint;
        this.usedPoint = usedPoint;
        this.point = point;
    }
}
