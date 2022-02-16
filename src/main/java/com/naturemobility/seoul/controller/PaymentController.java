package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.payment.*;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import com.naturemobility.seoul.domain.payment.refund.PostApproveRefundReq;
import com.naturemobility.seoul.domain.payment.refund.PostApproveRefundRes;
import com.naturemobility.seoul.domain.payment.refund.PostReqRefundReq;
import com.naturemobility.seoul.domain.payment.refund.PostReqRefundRes;
import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import com.naturemobility.seoul.service.payment.PaymentService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pay")
public class PaymentController {
    @Autowired
    CheckUserService checkUserService;
    @Autowired
    PaymentService  paymentService;

    @PostMapping("/request_billingKeyPayment")
    BaseResponse<PostClientPayRes> requestBillingKeyPayment(@RequestBody PostPayReq postPayReq)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        PostClientPayRes result = paymentService.billingKeyPayment(postPayReq, userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/request_payment")
    BaseResponse<PostClientPayRes> requestPayment(@RequestBody PostGeneralPayReq postGeneralPayReq)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        PostClientPayRes result = paymentService.payment(postGeneralPayReq, userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/request_refund")
    BaseResponse<PostReqRefundRes> requestRefund(@RequestBody PostReqRefundReq postReqRefundReq) throws Exception{
        Long userIdx = checkUserService.getUserIdx();
        PostReqRefundRes result = paymentService.requestRefund(postReqRefundReq, userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/approve_refund")
    BaseResponse<PostApproveRefundRes> approveRefund(@RequestBody PostApproveRefundReq postApproveRefund) throws Exception{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostApproveRefundRes result = paymentService.approveRefund(postApproveRefund, partnerIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }
}
