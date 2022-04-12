package com.naturemobility.seoul.domain.calculateManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCalculationListRes {
    final Long partnerPaymentIdx;
    final Integer sales;
    final Integer fees;
    final Integer price;
    final String calculateStatus;
    final String calculateDate;
    final String createdAt;
    final String calculatedAt;
}
