package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.userPayment.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    int savePayment(PaymentInfo paymentInfo);
}
