package com.naturemobility.seoul.service.users;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.secret.Secret;
import com.naturemobility.seoul.domain.users.*;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.utils.AES128;
import com.naturemobility.seoul.utils.JwtService;
import com.naturemobility.seoul.domain.users.UserStatus;
import com.naturemobility.seoul.utils.ValidationRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;


@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private JwtService jwtService;

    /**
     * 중복 회원 검증
     * @param userEmail
     * @return String
     * @throws BaseException
     */
    @Override
    public String checkEmail(String userEmail) throws BaseException {
        //false 이면 중복, true 이면 중복 X
        UserInfo userInfo =null;
        try {
            userInfo = retrieveUserInfoByEmail(userEmail);
            log.info(userInfo.toString());
        } catch (BaseException exception) {
            log.info(exception.getStatus().toString());
            if (exception.getStatus() != NOT_FOUND_USER)
                throw exception;
        }
        if (userInfo != null)
            throw new BaseException(DUPLICATED_EMAIL);
        else  return userEmail+"_valid";
    }


    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    @Override
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UserInfo existsEmail = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsEmail = retrieveUserInfoByEmail(postUserReq.getEmail());
            if (existsEmail != null) log.info(existsEmail.toString());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsEmail != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String email = postUserReq.getEmail();
        String nickname = postUserReq.getNickname();
        String name = postUserReq.getName();
        String password;
//        String id = postUserReq.getId();
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
        UserInfo userInfo = new UserInfo(email, password, nickname, name);

        // 3. 유저 정보 저장
        try {
            usersMapper.save(userInfo);
        } catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 4. JWT 생성
        String jwt = jwtService.createJwt(userInfo.getUserIdx());

        // 5. DTOUsersLoginRes로 변환하여 return
        Long idx = userInfo.getUserIdx();
        return new PostUserRes(idx, jwt);
    }

    /** 회원 정보 수정
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    @Override
    public PatchUserRes updateUserInfo(Long userIdx, PatchUserReq patchUserReq) throws BaseException {
        try {
            UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(()-> new BaseException(NOT_FOUND_USER));
            String nickname = patchUserReq.getNickname()+"_edited";
            String name = patchUserReq.getName()+"_edited";
            String image = patchUserReq.getUserImage()+"_edited";
            log.info(userInfo.toString());

            if (patchUserReq.getName() != null && patchUserReq.getName().length() != 0)
                userInfo.setUserName(patchUserReq.getName());
            if (patchUserReq.getNickname() != null && patchUserReq.getNickname().length() != 0)
                userInfo.setUserNickname(patchUserReq.getNickname());
            if (patchUserReq.getUserImage() != null && patchUserReq.getNickname().length() != 0)
                userInfo.setUserName(patchUserReq.getUserImage());

            usersMapper.update(userInfo);
            return new PatchUserRes(nickname, name,image);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }

    /** 비밀번호 수정
     * @param userIdx, patchPWReq
     * @throws BaseException
     */
    @Override
    public void updatePW(Long userIdx, PatchPWReq patchPWReq) throws BaseException {
        UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        //기존 비밀번호 일치 여부 확인
        if (!patchPWReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }
        //새로운 비밀번호 일치 확인
        if (!patchPWReq.getNewPassword().equals(patchPWReq.getConfirmNewPassword())) {
            throw new BaseException(WRONG_PASSWORD);
        }

        //암호화
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchPWReq.getNewPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        //변경사항 저장
        userInfo.setUserPassword(password);
        try {
            usersMapper.update(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
        /**
         * 회원 탈퇴
         * @param userIdx
         * @throws BaseException
         */
    @Override
    public void deleteUserInfo(Long userIdx) throws BaseException {
        // 1. 존재하는 DTOUsers가 있는지 확인 후 저장
        UserInfo userInfo = retrieveUserInfoByUserIdx(userIdx);

        // 2-1. 해당 DTOUsers를 완전히 삭제
//        try {
//            usersMapper.delete(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 DTOUsers의 status를 WITHDRAWN로 설정
        userInfo.setUserStatus(UserStatus.WITHDRAWN.getValue());
        try {
            usersMapper.update(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
    /**
     * 전체 회원 조회
     * @return List<GetUsersRes>
     * @throws BaseException
     */
    @Override
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<UserInfo> userInfoList;
        try {
            if (word == null) { // 전체 조회
                userInfoList = usersMapper.findByStatus(UserStatus.ACTIVE.getValue());
            } else { // 검색 조회
                userInfoList = usersMapper.findByStatusAndNicknameIsContaining(UserStatus.ACTIVE.getValue(), word);
            }
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            Long idx = userInfo.getUserIdx();
            String email = userInfo.getUserEmail();
            return new GetUsersRes(idx, email);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userIdx
     * @return GetUserRes
     * @throws BaseException
     */
    @Override
    public GetUserRes findUserInfo(Long userIdx) throws BaseException {
        // 1. DB에서 userIdx로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoByUserIdx(userIdx);

        // 2. UserInfoRes로 변환하여 return
        Long idx = userInfo.getUserIdx();
        String email = userInfo.getUserEmail();
        String nickname = userInfo.getUserNickname();
        String name = userInfo.getUserName();
        String image = userInfo.getUserImage();
        return new GetUserRes(idx, email, nickname, name, image);
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        UserInfo userInfo = new UserInfo();
        // 1. DB에서 UserInfo 조회
        String userId=postLoginReq.getId();
        if (!ValidationRegex.isRegexEmail(userId))
            throw new BaseException(INVALID_EMAIL);
        else userInfo = retrieveUserInfoByEmail(userId);

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!postLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createJwt(userInfo.getUserIdx());

        // 4. PostLoginRes 변환하여 return
        Long idx = userInfo.getUserIdx();
        return new PostLoginRes(idx, jwt);
    }
    /**
     * 비밀번호 확인
     * @param userIdx, pw
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes checkPW(Long userIdx, String pw) throws BaseException {
        UserInfo userInfo = new UserInfo();
        // 1. DB에서 UserInfo 조회
        userInfo = retrieveUserInfoByUserIdx(userIdx);

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!pw.equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createJwt(userInfo.getUserIdx());

        // 4. PostLoginRes 변환하여 return
        Long idx = userInfo.getUserIdx();
        return new PostLoginRes(idx, jwt);
    }

    /**
     * 회원 조회
     * @param userIdx
     * @return DTOUsers
     * @throws BaseException
     */
    @Override
    public UserInfo retrieveUserInfoByUserIdx(Long userIdx) throws BaseException {
        // 1. DB에서 UserInfo 조회
        UserInfo existsDTOUser;
        existsDTOUser = usersMapper.findByIdx(userIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));

        // 2. 존재하는 회원인지 확인
        UserInfo userInfo =null;
        if (existsDTOUser != null) {
            if(existsDTOUser.getUserStatus().equals(UserStatus.ACTIVE.getValue()))
                userInfo =existsDTOUser;
            else if(existsDTOUser.getUserStatus().equals(UserStatus.INACTIVE.getValue()))
                throw new BaseException(INACTIVE_ID);
            else if(existsDTOUser.getUserStatus().equals(UserStatus.WITHDRAWN.getValue()))
                throw new BaseException(NOT_FOUND_USER);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    /**
     * 회원 조회
     * @param email
     * @return DTOUsers
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException {
        List<UserInfo> existsDTOUser = new ArrayList<>();
        UserInfo userInfo = null;
        existsDTOUser = usersMapper.findByEmailAndStatus(email, UserStatus.ACTIVE.getValue());

        if (existsDTOUser.size()>0) userInfo = existsDTOUser.get(0);
        else {
            existsDTOUser = usersMapper.findByEmailAndStatus(email, UserStatus.INACTIVE.getValue());
            if (existsDTOUser.size() > 0) {
                log.info(existsDTOUser.size()+" 개");
                throw new BaseException(INACTIVE_ID);
            }
            else
                throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }

    /** 마이페이지
     * @param userIdx
     * @return GetMyPageRes
     * @throws BaseException
     */
    public GetMyPageRes myPage(Long userIdx) throws BaseException {
        UserInfo user;
        Integer point=0, cntReservation=0, cntStoreLike=0, cntCoupon=0;
        String image=null, nickName=null;
        user = usersMapper.findByIdx(userIdx).orElseThrow(()-> new BaseException(RESPONSE_ERROR));
        if (user!=null) {
            nickName = user.getUserNickname();
            image = user.getUserImage();
            point = user.getUserPoint();
            cntReservation = usersMapper.cntReservation(userIdx).orElseGet(()->0);
            cntCoupon = usersMapper.cntCoupon(userIdx).orElseGet(()->0);
            cntStoreLike = usersMapper.cntStoreLike(userIdx).orElseGet(()->0);
        }
        GetMyPageRes getMyPageRes = new GetMyPageRes(image,nickName,cntReservation, cntStoreLike, point,cntCoupon);
        return getMyPageRes;
    }

}
