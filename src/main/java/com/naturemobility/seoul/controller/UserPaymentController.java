package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.userPayment.*;
import com.naturemobility.seoul.service.userpayment.UserPaymentService;
import com.naturemobility.seoul.utils.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("payment")
public class UserPaymentController {
    @Autowired
    UserPaymentService userPaymentService;
    @Autowired
    CheckUserService checkUserService;

    @GetMapping("billingKey")
    BaseResponse<Map<String,String>> requestBillingKey() throws Exception{
        Map<String, String> result = userPaymentService.createBillingKey();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/register_card")
    BaseResponse<PostUserPaymentRes> registerUserPayment(@RequestParam("billingKey") String billingKey)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        PostUserPaymentRes postUserPaymentRes = userPaymentService.registerUserPayment(billingKey, userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, postUserPaymentRes);
    }

    @PostMapping("/request_payment")
    BaseResponse<Map<String,String>> requestPayment(@RequestBody PostPayReq postPayReq)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        Map<String, String> result = userPaymentService.payment(postPayReq, userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @GetMapping("/user_payments")
    BaseResponse<List<GetUserPayments>> getUserPayments() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        List<GetUserPayments> result =userPaymentService.getUserPayments(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }
    @GetMapping("/customerUid/{uid}")
    BaseResponse<Map<String,String>> getcustomerUid(@PathVariable("uid") Long uid) throws Exception {
        Map<String,String> result = userPaymentService.getBillingKey(uid);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }
    @PostMapping("/update_main")
    BaseResponse<PostMain> updateMain(@RequestBody PostMain postMain) throws Exception {
        PostMain result = userPaymentService.updateMain(postMain);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

}
