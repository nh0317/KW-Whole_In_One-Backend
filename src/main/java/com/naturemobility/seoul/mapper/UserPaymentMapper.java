package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.userPayment.GetUserPayments;
import com.naturemobility.seoul.domain.userPayment.PaymentInfo;
import com.naturemobility.seoul.domain.userPayment.UserPaymentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserPaymentMapper {
    int saveUserPayment(UserPaymentInfo userPaymentInfo);
    int updateMain(@Param("isMain") Boolean isMain, @Param("userPaymentIdx") Long userPaymentIdx);
    Optional<String> findUserPayment(Long userPaymentIdx);
    List<GetUserPayments> findAllUserPayments(@Param("userIdx") Long userIdx);
}
