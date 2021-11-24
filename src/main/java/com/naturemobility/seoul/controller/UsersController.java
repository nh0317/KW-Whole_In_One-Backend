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
    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUsersRes> getUsersResList = usersService.retrieveUserInfoList(word);
            return new BaseResponse<>(SUCCESS, getUsersResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이페이지 API
     * [GET] /users/mypage
     * @return BaseResponse<GetMyPageRes>
     */
    @ResponseBody
    @GetMapping("mypage")
    public BaseResponse<GetMyPageRes> getMyPage() {
        Long userIdx;
        try {
            userIdx = checkUserService.getUserIdx();
            GetMyPageRes getMyPageRes = usersService.myPage(userIdx);
            return new BaseResponse<>(SUCCESS, getMyPageRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 개인 정보 조회 API
     * [GET] /users/mypage/edit
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("mypage/edit")
    public BaseResponse<GetUserRes> getUser() {
        try {
            Long userIdx = checkUserService.getUserIdx();
            GetUserRes getUserRes = usersService.findUserInfo(userIdx);
            return new BaseResponse<>(SUCCESS, getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 회원 정보 수정 API (id는 수정 불가, 비밀번호는 따로 수정)
     * [PATCH] /users/mypage/edit
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRe>
     */
    @ResponseBody
    @PatchMapping("mypage/edit")
    public BaseResponse<PatchUserRes> pathUser(@RequestBody PatchUserReq parameters) {
        try {
            UserInfo userInfo = checkUserService.getUser();
            return new BaseResponse<>(SUCCESS, usersService.updateUserInfo(userInfo, parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 비밀번호 수정 (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Patch] /users/mypage/edit_password
     * @RequestBody PatchPWReq
     * @return PostCheck
     */
    @ResponseBody
    @PatchMapping("mypage/edit_password")
    public BaseResponse<Void> editPW(@RequestBody PatchPWReq patchPWReq) {
        // 2. Post UserInfo
        try {
            UserInfo userInfo = checkUserService.getUser();
            usersService.updatePW(userInfo,patchPWReq);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS);
    }
    /**
     * 이메일 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /users/sign_up/check_userEmail
     * @RequestParam email
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("sign_up/check_email")
    public BaseResponse<String> checkEmail(@RequestParam("email") String userEmail){
        String check ="";
        try {
            check = usersService.checkEmail(userEmail);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        // 2. Post UserInfo
        return new BaseResponse<>(SUCCESS,check);
    }

    /**
     * 회원가입 API
     * [POST] /users/sign_up
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("sign_up")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
        // 1. Body Parameter Validation
        if (!isRegexEmail(parameters.getEmail())){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }

        // 2. Post UserInfo
        try {
            PostUserRes postUserRes = usersService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 확인 API (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Post] /users/check_password
     * @RequestParam password
     * BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("check_password")
    public BaseResponse<PostLoginRes> confirmPW(@RequestParam("password") String password) {
        // 2. Post UserInfo
        PostLoginRes postLoginRes;
        try {
            String email = checkUserService.getEmail();
            postLoginRes = usersService.checkPW(email,password);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS,postLoginRes);
    }

    /**
     * 로그인 API
     * [POST] /users/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping(value="login" )
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        log.info("아이디 : "+postLoginReq.getId());
        log.info("패스워드 : "+postLoginReq.getPassword());
        // 1. Body Parameter Validation
        if (postLoginReq.getId() == null || postLoginReq.getId().length() == 0){
            return new BaseResponse<>(REQUEST_ERROR);
        } else if (postLoginReq.getPassword() == null || postLoginReq.getPassword().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        // 2. Login
        try {
            PostLoginRes postLoginRes = usersService.login(postLoginReq);
            return new BaseResponse<>(SUCCESS, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/withdraw
     * @return BaseResponse<Void>
     */
    @DeleteMapping("withdraw")
    public BaseResponse<Void> deleteUsers() {
        try {
            UserInfo userInfo = checkUserService.getUser();
            usersService.deleteUserInfo(userInfo);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * JWT 검증 API
     * [GET] /users/jwt
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
}
