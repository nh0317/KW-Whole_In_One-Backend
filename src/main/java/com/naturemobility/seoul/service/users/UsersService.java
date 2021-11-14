package com.naturemobility.seoul.service.users;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.users.*;

import java.util.List;

public interface UsersService {
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException;
    public String checkEmail(String userEmail) throws BaseException;
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException;
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException;
    public PostLoginRes checkPW(Long userIdx, String pw) throws BaseException;
    public void updatePW(Long userIdx, PatchPWReq patchPWReq) throws BaseException;
    public GetMyPageRes myPage(Long userIdx) throws BaseException;
    public GetUserRes findUserInfo(Long userIdx) throws BaseException;
    public PatchUserRes updateUserInfo(Long userIdx, PatchUserReq patchUserReq) throws BaseException;
    public void deleteUserInfo(Long userIdx) throws BaseException;
    public UserInfo retrieveUserInfoByUserIdx(Long userIdx) throws BaseException;
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException;
}
