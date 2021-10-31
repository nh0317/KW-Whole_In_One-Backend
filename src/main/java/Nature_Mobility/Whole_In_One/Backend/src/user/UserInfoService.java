package Nature_Mobility.Whole_In_One.Backend.src.user;//package com.softsquared.template.src.user;
//
//import com.softsquared.template.utils.JwtService;
//import com.softsquared.template.config.secret.Secret;
//import com.softsquared.template.utils.AES128;
//import com.softsquared.template.config.BaseException;
//import com.softsquared.template.src.user.models.*;
//import lombok.NonNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import static com.softsquared.template.config.BaseResponseStatus.*;
//
//
//@Service
//public class UserInfoService {
//    private final UserInfoRepository userInfoRepository;
//    private final UserInfoProvider userInfoProvider;
//    private final JwtService jwtService;
//
//    @Autowired
//    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService) {
//        this.userInfoRepository = userInfoRepository;
//        this.userInfoProvider = userInfoProvider;
//        this.jwtService = jwtService;
//    }
//
//    /**
//     * 회원가입
//     * @param postUserReq
//     * @return PostUserRes
//     * @throws BaseException
//     */
//    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
//        UserInfo existsUserInfo = null;
//        try {
//            // 1-1. 이미 존재하는 회원이 있는지 조회
//            existsUserInfo = userInfoProvider.retrieveUserInfoByEmail(postUserReq.getEmail());
//        } catch (BaseException exception) {
//            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
//            if (exception.getStatus() != NOT_FOUND_USER) {
//                throw exception;
//            }
//        }
//        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
//        if (existsUserInfo != null) {
//            throw new BaseException(DUPLICATED_USER);
//        }
//
//        // 2. 유저 정보 생성
//        String email = postUserReq.getEmail();
//        String nickname = postUserReq.getNickname();
//        String phoneNumber = postUserReq.getPhoneNumber();
//        String password;
//        try {
//            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
//        } catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_POST_USER);
//        }
//        UserInfo userInfo = new UserInfo(email, password, nickname, phoneNumber);
//
//        // 3. 유저 정보 저장
//        try {
//            userInfo = userInfoRepository.save(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(FAILED_TO_POST_USER);
//        }
//
//        // 4. JWT 생성
//        String jwt = jwtService.createJwt(userInfo.getId());
//
//        // 5. UserInfoLoginRes로 변환하여 return
//        int id = userInfo.getId();
//        return new PostUserRes(id, jwt);
//    }
//
//    /**
//     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
//     * @param patchUserReq
//     * @return PatchUserRes
//     * @throws BaseException
//     */
//    public PatchUserRes updateUserInfo(@NonNull Integer userId, PatchUserReq patchUserReq) throws BaseException {
//        try {
//            String email = patchUserReq.getEmail().concat("_edited");
//            String nickname = patchUserReq.getNickname().concat("_edited");
//            String phoneNumber = patchUserReq.getPhoneNumber().concat("_edited");
//            return new PatchUserRes(email, nickname, phoneNumber);
//        } catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_PATCH_USER);
//        }
//    }
//
//    /**
//     * 회원 탈퇴
//     * @param userId
//     * @throws BaseException
//     */
//    public void deleteUserInfo(int userId) throws BaseException {
//        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
//        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userId);
//
//        // 2-1. 해당 UserInfo를 완전히 삭제
////        try {
////            userInfoRepository.delete(userInfo);
////        } catch (Exception exception) {
////            throw new BaseException(DATABASE_ERROR_USER_INFO);
////        }
//
//        // 2-2. 해당 UserInfo의 status를 INACTIVE로 설정
//        userInfo.setStatus("INACTIVE");
//        try {
//            userInfoRepository.save(userInfo);
//        } catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_DELETE_USER);
//        }
//    }
//}