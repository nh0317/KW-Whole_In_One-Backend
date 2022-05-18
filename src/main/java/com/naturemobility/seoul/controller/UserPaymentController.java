package com.naturemobility.seoul.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.userPayment.*;
import com.naturemobility.seoul.service.userpayment.UserPaymentService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("payment")
public class UserPaymentController {
    @Autowired
    UserPaymentService userPaymentService;
    @Autowired
    CheckUserService checkUserService;

    @GetMapping("billingKey")
    @ResponseBody
    BaseResponse<Map<String,String>> requestBillingKey() throws Exception{
        Long userIdx = checkUserService.getUserIdx();
        Map<String, String> result = userPaymentService.createBillingKey(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/register_card")
    @ResponseBody
    BaseResponse<PostUserPaymentRes> registerUserPayment(@RequestBody PostRegisterCardReq billingKey)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        PostUserPaymentRes postUserPaymentRes = userPaymentService.registerUserPayment(billingKey.getBillingKey(), userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, postUserPaymentRes);
    }

    @GetMapping("/m_register_card")
    String registerMobileUserPayment(@RequestParam("billingKey") String billingKey,
                                           @RequestParam("imp_success") Boolean imp_success,
                                           @RequestParam(required = false, value = "error_msg") String error_msg,
                                     Model model)
            throws Exception {
        Long userIdx = checkUserService.getUserIdx();
        if (!imp_success) {
            model.addAttribute("message",error_msg);
        }else{
            try {
                PostUserPaymentRes postUserPaymentRes = userPaymentService.registerUserPayment(billingKey, userIdx);
                model.addAttribute("message","결제 수단 등록에 성공했습니다. ");
            } catch (Exception e) {
                model.addAttribute("message","결제 수단 등록에 실패했습니다. ");
                return "Message";
            }
        }
        model.addAttribute("href","payment");
        return "Message";
    }

    @GetMapping("/user_payments")
    @ResponseBody
    BaseResponse<List<GetUserPayments>> getUserPayments() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        List<GetUserPayments> result =userPaymentService.getUserPayments(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @DeleteMapping("/user_payment/{userPaymentIdx}")
    @ResponseBody
    BaseResponse<Void> deleteCustomerUid(@PathVariable("userPaymentIdx") Long uid) throws Exception {
        userPaymentService.deleteUserPayment(uid);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/update_main")
    @ResponseBody
    BaseResponse<PostMain> updateMain(@RequestBody PostMain postMain) throws BaseException {
        PostMain result = userPaymentService.updateMain(postMain);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @PostMapping("/get_main")
    @ResponseBody
    BaseResponse<GetUserPayments> getMain() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        GetUserPayments result = userPaymentService.getMain(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @GetMapping("/customerUid/{uid}")
    @ResponseBody
    BaseResponse<Map<String,String>> getCustomerUid(@PathVariable("uid") Long uid) throws Exception {
        Map<String,String> result = userPaymentService.getBillingKey(uid);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }

    @GetMapping("/get_main")
    @ResponseBody
    BaseResponse<GetUserPayments> getMainCard() throws BaseException{
        Long userIdx = checkUserService.getUserIdx();
        GetUserPayments result = userPaymentService.getMain(userIdx);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, result);
    }
}
