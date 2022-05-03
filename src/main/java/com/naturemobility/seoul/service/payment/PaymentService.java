package com.naturemobility.seoul.service.payment;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.payment.*;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import com.naturemobility.seoul.domain.payment.refund.*;
import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PaymentService {

    @Transactional
    PostClientPayRes payment(PostGeneralPayReq postGeneralPayReq, Long userIdx) throws Exception;

    @Transactional
    PostClientPayRes billingKeyPayment(PostPayReq postPayReq, Long userIdx) throws Exception;

    @Transactional
    PostClientPayRes offlinePayment(PostGeneralPayReq postGeneralPayReq, Long userIdx) throws Exception;

    PostReqRefundRes requestRefund(PostReqRefundReq postRefundReq, Long userIdx) throws BaseException;

    @Transactional
    PostApproveRefundRes approveRefund(PostApproveRefundReq postApproveRefund, Long partnerIdx) throws BaseException;

    GetUserInfoInPayment getUserInfo(Long userIdx) throws BaseException;

    List<GetRefunsResNoPaging> getRefundsList(Long partnerIdx) throws BaseException;

    GetPagingRefunds getPagingRefundsList(Long partnerIdx, Integer page, String status) throws BaseException;
}
