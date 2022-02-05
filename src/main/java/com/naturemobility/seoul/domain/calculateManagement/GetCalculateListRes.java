package com.naturemobility.seoul.domain.calculateManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCalculateListRes {
    final Integer reservePrice;
    final Integer discountPrice;
    final Integer payPrice;
    final String calculateStatus;
    final String createdAt;
    final String paymentTime;
}
