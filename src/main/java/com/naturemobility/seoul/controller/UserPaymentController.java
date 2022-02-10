package com.naturemobility.seoul.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
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
        Long userIdx = checkUserService.getUserIdx();
        Map<String, String> result = userPaymentService.createBillingKey(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/register_card")
    BaseResponse<PostUserPaymentRes> registerUserPayment(@RequestBody PostRegisterCardReq billingKey)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        PostUserPaymentRes postUserPaymentRes = userPaymentService.registerUserPayment(billingKey.getBillingKey(), userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, postUserPaymentRes);
    }

    @GetMapping("/user_payments")
    BaseResponse<List<GetUserPayments>> getUserPayments() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        List<GetUserPayments> result =userPaymentService.getUserPayments(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @DeleteMapping("/user_payment/{userPaymentIdx}")
    BaseResponse<Void> deleteCustomerUid(@PathVariable("userPaymentIdx") Long uid) throws Exception {
        userPaymentService.deleteUserPayment(uid);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/update_main")
    BaseResponse<PostMain> updateMain(@RequestBody PostMain postMain) throws BaseException {
        PostMain result = userPaymentService.updateMain(postMain);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/get_main")
    BaseResponse<GetUserPayments> getMain() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        GetUserPayments result = userPaymentService.getMain(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @GetMapping("/customerUid/{uid}")
    BaseResponse<Map<String,String>> getCustomerUid(@PathVariable("uid") Long uid) throws Exception {
        Map<String,String> result = userPaymentService.getBillingKey(uid);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @GetMapping("/get_main")
    BaseResponse<GetUserPayments> getMainCard() throws BaseException{
        Long userIdx = checkUserService.getUserIdx();
        GetUserPayments result = userPaymentService.getMain(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }
}
