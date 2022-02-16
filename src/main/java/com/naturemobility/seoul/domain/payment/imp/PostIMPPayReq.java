package com.naturemobility.seoul.domain.payment.imp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostIMPPayReq {
    private final String customer_uid;
    private final String merchant_uid;
    private final int amount;
    private final String name;
}
