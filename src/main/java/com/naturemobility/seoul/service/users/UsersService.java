package com.naturemobility.seoul.service.users;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.users.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UsersService {
    List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException;
    PostLoginRes login(HttpServletResponse response, PostLoginReq postLoginReq) throws BaseException;
    String checkEmail(String userEmail) throws BaseException;
    GetMyPageRes myPage(Long userIdx) throws BaseException;
    PatchUserRes updateUserInfo(UserInfo userInfo, PatchUserReq patchUserReq) throws BaseException;
    void updatePW(HttpServletResponse res, UserInfo userInfo, PatchPWReq patchPWReq) throws BaseException;
    void deleteUserInfo(UserInfo userInfo) throws BaseException;
    GetUserRes findUserInfo(Long userIdx) throws BaseException;
    PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException;

    void logout(HttpServletRequest req, HttpServletResponse res) throws BaseException;

    PostLoginRes checkPW(HttpServletResponse response, String userEmail, String pw) throws BaseException;
    UserInfo retrieveUserInfoByUserIdx(Long userIdx) throws BaseException;
    UserInfo retrieveUserInfoByEmail(String email) throws BaseException;

    PostLoginRes refreshToken(HttpServletRequest request, HttpServletResponse response) throws BaseException;

    void postCoupon(Long userIdx,Long couponIdx) throws BaseException;

    List<GetUserCoupon> getUserCoupon (Long userIdx) throws BaseException;
}
