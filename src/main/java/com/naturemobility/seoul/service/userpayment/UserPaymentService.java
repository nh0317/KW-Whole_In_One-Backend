package com.naturemobility.seoul.service.userpayment;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.userPayment.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserPaymentService {
    @Transactional
    PostUserPaymentRes registerUserPayment(String billingKey, Long userIdx)  throws Exception;

    Map<String,String> createBillingKey(Long userIdx) throws Exception;

    List<GetUserPayments> getUserPayments(Long userIdx) throws BaseException;
    PostMain updateMain(PostMain postMain) throws BaseException;

    GetUserPayments getMain(Long userIdx) throws BaseException;

    @Transactional
    void deleteUserPayment(Long userPaymentIdx) throws Exception;

    Map<String,String> getBillingKey(Long userPaymentIdx) throws Exception;
}
