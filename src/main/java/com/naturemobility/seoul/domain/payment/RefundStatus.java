package com.naturemobility.seoul.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum RefundStatus {
    NOT_REFUND(0,"미환불"),
    REQUEST_REFUND(1,"환불 요청"),
    COMPLETE(2,"환불 완료");
    private final int value;
    private final String msg;
    private RefundStatus(int value,String msg) {
        this.value = value;
        this.msg=msg;
    }

    @Override
    public String toString(){
        return this.msg;
    }
}
