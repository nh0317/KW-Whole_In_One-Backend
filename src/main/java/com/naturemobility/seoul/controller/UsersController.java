package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.users.*;
import com.naturemobility.seoul.service.users.UsersService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RestController
@RequestMapping("users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private CheckUserService checkUserService;

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     *
     * @return BaseResponse<List < GetUsersRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) throws BaseException {
        List<GetUsersRes> getUsersResList = usersService.retrieveUserInfoList(word);
        return new BaseResponse<>(SUCCESS, getUsersResList);
    }

    /**
     * 마이페이지 API
     * [GET] /users/mypage
     *
     * @return BaseResponse<GetMyPageRes>
     */
    @ResponseBody
    @GetMapping("mypage")
    public BaseResponse<GetMyPageRes> getMyPage() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        GetMyPageRes getMyPageRes = usersService.myPage(userIdx);
        return new BaseResponse<>(SUCCESS, getMyPageRes);
    }

    /**
     * 회원 개인 정보 조회 API
     * [GET] /users/mypage/edit
     *
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("mypage/edit")
    public BaseResponse<GetUserRes> getUser() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        GetUserRes getUserRes = usersService.findUserInfo(userIdx);
        return new BaseResponse<>(SUCCESS, getUserRes);
    }

    /**
     * 회원 정보 수정 API (id는 수정 불가, 비밀번호는 따로 수정)
     * [PATCH] /users/mypage/edit
     *
     * @return BaseResponse<PatchUserRe>
     * @RequestBody PatchUserReq
     */
    @ResponseBody
    @PatchMapping("mypage/edit")
    public BaseResponse<PatchUserRes> pathUser(@RequestBody PatchUserReq parameters) throws BaseException {
        UserInfo userInfo = checkUserService.getUser();
        return new BaseResponse<>(SUCCESS, usersService.updateUserInfo(userInfo, parameters));
    }

    /**
     * 비밀번호 수정 (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Patch] /users/mypage/edit_password
     *
     * @return PostCheck
     * @RequestBody PatchPWReq
     */
    @ResponseBody
    @PatchMapping("mypage/edit_password")
    public BaseResponse<Void> editPW(HttpServletResponse response, @RequestBody PatchPWReq patchPWReq) throws BaseException {
        // 2. Post UserInfo
        UserInfo userInfo = checkUserService.getUser();
        usersService.updatePW(response, userInfo, patchPWReq);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 이메일 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /users/sign_up/check_userEmail
     *
     * @return BaseResponse<String>
     * @RequestParam email
     */
    @ResponseBody
    @PostMapping("sign_up/check_email")
    public BaseResponse<String> checkEmail(@RequestParam("email") String userEmail) throws BaseException {
        String check = usersService.checkEmail(userEmail);
        // 2. Post UserInfo
        return new BaseResponse<>(SUCCESS, check);
    }

    /**
     * 회원가입 API
     * [POST] /users/sign_up
     *
     * @return BaseResponse<PostUserRes>
     * @RequestBody PostUserReq
     */
    @ResponseBody
    @PostMapping("sign_up")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) throws BaseException {
        // 1. Body Parameter Validation
        if (!isRegexEmail(parameters.getEmail()))
            return new BaseResponse<>(INVALID_EMAIL);
        if (!parameters.getPassword().equals(parameters.getConfirmPassword()))
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);

        // 2. Post UserInfo
        PostUserRes postUserRes = usersService.createUserInfo(parameters);
        return new BaseResponse<>(SUCCESS, postUserRes);
    }

    /**
     * 비밀번호 확인 API (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Post] /users/check_password
     *
     * @RequestParam password
     * BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("check_password")
    public BaseResponse<PostLoginRes> confirmPW(HttpServletResponse response, @RequestParam("password") String password) throws BaseException {
        // 2. Post UserInfo
        PostLoginRes postLoginRes = usersService.checkPW(response, checkUserService.getEmail(), password);
        return new BaseResponse<>(SUCCESS, postLoginRes);
    }

    /**
     * 로그인 API
     * [POST] /users/login
     *
     * @return BaseResponse<PostLoginRes>
     * @RequestBody PostLoginReq
     */
    @ResponseBody
    @PostMapping(value = "login")
    public BaseResponse<PostLoginRes> login(HttpServletResponse response, @RequestBody PostLoginReq postLoginReq) throws BaseException {
        log.info("아이디 : " + postLoginReq.getId());
        log.info("패스워드 : " + postLoginReq.getPassword());
        // 1. Body Parameter Validation
        if (postLoginReq.getId() == null || postLoginReq.getId().length() == 0)
            return new BaseResponse<>(REQUEST_ERROR);
        else if (postLoginReq.getPassword() == null || postLoginReq.getPassword().length() == 0)
            return new BaseResponse<>(REQUEST_ERROR);

        // 2. Login
        PostLoginRes postLoginRes = usersService.login(response, postLoginReq);
        return new BaseResponse<>(SUCCESS, postLoginRes);
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/withdraw
     *
     * @return BaseResponse<Void>
     */
    @DeleteMapping("withdraw")
    public BaseResponse<Void> deleteUsers() throws BaseException {
        UserInfo userInfo = checkUserService.getUser();
        usersService.deleteUserInfo(userInfo);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * JWT 검증 API
     * [GET] /users/jwt
     *
     * @return BaseResponse<Void>
     */
//    @GetMapping("/jwt")
//    public BaseResponse<Void> jwt() {
//        try {
//            Long userIdx = jwtService.getUserIdx();
//            usersProvider.retrieveUserInfo(userIdx);
//            return new BaseResponse<>(SUCCESS_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
    @PostMapping("/refresh")
    public BaseResponse<PostLoginRes> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws BaseException {
        PostLoginRes result = usersService.refreshToken(request, response);
        return new BaseResponse<>(SUCCESS, result);
    }

    /**
     * 쿠폰 받기 API
     * [Post] /coupon?couponIdx=1
     *
     * @return BaseResponse
     * @RequestParam couponIdx
     */
    @ResponseBody
    @PostMapping("coupon")
    public BaseResponse postCoupon(@RequestParam("couponIdx") Long couponIdx) throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        usersService.postCoupon(userIdx, couponIdx);
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(HttpServletRequest req, HttpServletResponse res) throws BaseException {
        usersService.logout(req, res);
        return new BaseResponse<>(SUCCESS);
    }

    @GetMapping("coupon")
    public BaseResponse<List<GetUserCoupon>> getUserCoupon() throws BaseException {
        Long userIdx = checkUserService.getUserIdx();
        List<GetUserCoupon> getUserCoupons = usersService.getUserCoupon(userIdx);
        return new BaseResponse<>(SUCCESS,getUserCoupons);
    }
}
