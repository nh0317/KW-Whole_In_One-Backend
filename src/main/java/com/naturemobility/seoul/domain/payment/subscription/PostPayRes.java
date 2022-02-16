package com.naturemobility.seoul.domain.payment.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class PostPayRes {
    private String imp_uid;
    private String merchant_uid;
}
