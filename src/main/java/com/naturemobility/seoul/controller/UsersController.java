package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.users.*;
import com.naturemobility.seoul.service.users.UsersService;
import com.naturemobility.seoul.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtService jwtService;

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
            if (word == null) {
                return new BaseResponse<>(SUCCESS, getUsersResList);
            } else {
                return new BaseResponse<>(SUCCESS, getUsersResList);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이페이지 API
     * [GET] /users/mypage
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<GetMyPageRes>
     */
    @ResponseBody
    @GetMapping("/mypage")
    public BaseResponse<GetMyPageRes> getMyPage(@RequestHeader("X-ACCESS-TOKEN") String token) {
        Long userIdx;
        try {
            userIdx = jwtService.getUserIdx();
            log.info(userIdx.toString());
            GetMyPageRes getMyPageRes = usersService.myPage(userIdx);
            return new BaseResponse<>(SUCCESS, getMyPageRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 개인 정보 조회 API
     * [GET] /users/mypage/edit
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/mypage/edit")
    public BaseResponse<GetUserRes> getUser(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            GetUserRes getUserRes = usersService.retrieveUserInfo(userIdx);
            return new BaseResponse<>(SUCCESS, getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 회원 정보 수정 API (id는 수정 불가, 비밀번호는 따로 수정)
     * [PATCH] /users/mypage/edit
     * @RequestBody PatchUserReq
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<PatchUserRe>
     */
    @ResponseBody
    @PatchMapping("/mypage/edit")
    public BaseResponse<PatchUserRes> getUser(@RequestBody PatchUserReq parameters , @RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            return new BaseResponse<>(SUCCESS, usersService.updateUserInfo(userIdx, parameters));
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
    @PatchMapping("/mypage/edit_password")
    public BaseResponse<Void> editPW(@RequestBody PatchPWReq patchPWReq, @RequestHeader("X-ACCESS-TOKEN") String token) {
        // 2. Post UserInfo
        PostLoginRes postLoginRes;
        try {
            Long userIdx = jwtService.getUserIdx();
            usersService.editPW(userIdx,patchPWReq);

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
    @PostMapping("sign_up/check_userEmail")
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
     * 아이디 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /users/sign_up/check_userId
     * @RequestParam userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("sign_up/check_userId")
    public BaseResponse<String> checkID(@RequestParam("userId") String userId) {
        String check ="";
        try {
            check = usersService.checkID(userId);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        // 2. Post UserInfo
        return new BaseResponse<>(SUCCESS,check);
    }

    /**
     * 회원가입 API
     * [POST] /sign_up
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("sign_up")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (!isRegexEmail(parameters.getEmail())){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }
        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        if (parameters.getName() == null || parameters.getName().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
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
     * @RequestHeader X-ACCESS-TOKEN
     * BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("check_password")
    public BaseResponse<PostLoginRes> confirmPW(@RequestParam("password") String password,
                                                @RequestHeader("X-ACCESS-TOKEN") String token) {
        // 2. Post UserInfo
        PostLoginRes postLoginRes;
        try {
            Long userIdx = jwtService.getUserIdx();
            postLoginRes = usersService.checkPW(userIdx,password);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS,postLoginRes);
    }

    //아이디 또는 이메일로 로그인 가능
    /**
     * 로그인 API
     * [POST] /users/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0){
            return new BaseResponse<>(REQUEST_ERROR);
        } else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        // 2. Login
        try {
            PostLoginRes postLoginRes = usersService.login(parameters);
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
    @DeleteMapping("/withdraw")
    public BaseResponse<Void> deleteUsers(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            usersService.deleteUserInfo(userIdx);
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
