package com.naturemobility.seoul.service.userpayment;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.userPayment.*;

import java.util.List;
import java.util.Map;

public interface UserPaymentService {
    PostUserPaymentRes registerUserPayment(String billingKey, Long userIdx)  throws Exception;

    Map<String,String> payment(PostPayReq postPayReq, Long userIdx) throws Exception;
    Map<String,String> createBillingKey() throws Exception;
    List<GetUserPayments> getUserPayments(Long userIdx) throws BaseException;

    Map<String,String> getBillingKey(Long userPaymentIdx) throws Exception;

    PostMain updateMain(PostMain postMain) throws Exception;

//    String getToken() throws BaseException;
}
