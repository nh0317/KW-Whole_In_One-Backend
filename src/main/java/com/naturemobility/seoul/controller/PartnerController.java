package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.partners.*;
import com.naturemobility.seoul.domain.users.PatchPWReq;
import com.naturemobility.seoul.domain.users.PostLoginReq;
import com.naturemobility.seoul.domain.users.PostLoginRes;
import com.naturemobility.seoul.service.partner.PartnerService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.config.BaseResponseStatus.REQUEST_ERROR;
import static com.naturemobility.seoul.utils.ValidationRegex.isRegexEmail;


@Slf4j
@RestController
@RequestMapping("partner")
public class PartnerController {
    @Autowired
    PartnerService partnerService;
    
    @Autowired
    CheckUserService checkUserService;
//    JwtService jwtService;
    /**
     * 회원가입 API
     * [POST] /partner/sign_up
     * @RequestBody PostPartnerReq
     * @return BaseResponse<PostPartnerRes>
     */
    @ResponseBody
    @PostMapping("sign_up")
    public BaseResponse<PostPartnerRes> postPartners(@RequestBody PostPartnerReq parameters) {
        // 1. Body Parameter Validation
        if (!isRegexEmail(parameters.getEmail())){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }

        // 2. Post PartnerInfo
        try {
            PostPartnerRes postPartnerRes = partnerService.createPartnerInfo(parameters);
            return new BaseResponse<>(SUCCESS, postPartnerRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 로그인 API
     * [POST] /partners/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0){
            return new BaseResponse<>(REQUEST_ERROR);
        } else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        // 2. Login
        try {
            PostLoginRes postLoginRes = partnerService.login(parameters);
            return new BaseResponse<>(SUCCESS, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 이메일 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /partners/sign_up/check_email
     * @RequestParam email
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("sign_up/check_email")
    public BaseResponse<String> checkEmail(@RequestParam("email") String partnerEmail){
        String check ="";
        try {
            check = partnerService.checkEmail(partnerEmail);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        // 2. Post PartnerInfo
        return new BaseResponse<>(SUCCESS,check);
    }
    
    /**
     * 회원 개인 정보 조회 API
     * [GET] /partner/mypage/edit
     * @return BaseResponse<GetPartnerRes>
     */
    @ResponseBody
    @GetMapping("mypage/edit")
    public BaseResponse<GetPartnerRes> getPartner() {

        try {
            PartnerInfo partnerInfo = checkUserService.getPartner();
            GetPartnerRes getPartnerRes
                    = new GetPartnerRes(partnerInfo.getPartnerIdx(),
                                        partnerInfo.getPartnerEmail(),
                                        partnerInfo.getPartnerName());
            return new BaseResponse<>(SUCCESS, getPartnerRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API (id는 수정 불가, 비밀번호는 따로 수정)
     * [PATCH] /partner/mypage/edit
     * @RequestBody PatchPartnerReq
     * @return BaseResponse<PatchPartnerRe>
     */
    @ResponseBody
    @PatchMapping("mypage/edit")
    public BaseResponse<PatchPartnerRes> getPartner(@RequestBody PatchPartnerReq parameters) {
        try {
            PartnerInfo partnerInfo = checkUserService.getPartner();
            return new BaseResponse<>(SUCCESS, partnerService.updatePartnerInfo(partnerInfo, parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    
    /**
     * 비밀번호 수정 (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Patch] /partner/mypage/edit_password
     * @RequestBody PatchPWReq
     * @return PostCheck
     */
    @ResponseBody
    @PatchMapping("mypage/edit_password")
    public BaseResponse<Void> editPW(@RequestBody PatchPWReq patchPWReq) {
        // 2. Post PartnerInfo
        PostLoginRes postLoginRes;
        try {
            PartnerInfo partnerInfo = checkUserService.getPartner();
            partnerService.updatePW(partnerInfo,patchPWReq);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS);
    }
    /**
     * 비밀번호 확인 API (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Post] /partner/check_password
     * @RequestParam password
     * BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("check_password")
    public BaseResponse<PostLoginRes> confirmPW(@RequestParam("password") String password) {

        // 2. Post PartnerInfo
        PostLoginRes postLoginRes;
        try {
            String partnerEmail = checkUserService.getEmail();
            postLoginRes = partnerService.checkPW(partnerEmail,password);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS,postLoginRes);
    }
    
    /**
     * 회원 탈퇴 API
     * [DELETE] /partner/withdraw
     * @return BaseResponse<Void>
     */
    @DeleteMapping("withdraw")
    public BaseResponse<Void> deletePartners() {
        try {
            PartnerInfo partnerInfo = checkUserService.getPartner();
            partnerService.deletePartnerInfo(partnerInfo);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
