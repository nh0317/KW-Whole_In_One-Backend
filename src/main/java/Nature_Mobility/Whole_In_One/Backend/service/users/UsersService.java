package Nature_Mobility.Whole_In_One.Backend.service.users;

import Nature_Mobility.Whole_In_One.Backend.config.BaseException;
import Nature_Mobility.Whole_In_One.Backend.config.secret.Secret;
import Nature_Mobility.Whole_In_One.Backend.domain.users.*;
import Nature_Mobility.Whole_In_One.Backend.mapper.UsersMapper;
import Nature_Mobility.Whole_In_One.Backend.utils.AES128;
import Nature_Mobility.Whole_In_One.Backend.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static Nature_Mobility.Whole_In_One.Backend.config.BaseResponseStatus.*;
import static Nature_Mobility.Whole_In_One.Backend.domain.UserStatus.WITHDRAWN;


@Service
@Slf4j
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsersProvider usersProvider;

    /**
     * 중복 회원 검증
     * @param userEmail
     * @return String
     * @throws BaseException
     */
    public String checkEmail(String userEmail) throws BaseException {
        //false 이면 중복, true 이면 중복 X
        UserInfo userInfo =null;
        try {
            userInfo = usersProvider.retrieveUserInfoByEmail(userEmail);
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
     * 중복 회원 검증
     * @param userID
     * @return String
     * @throws BaseException
     */
    public String checkID(String userID) throws BaseException {
        //false 이면 중복, true 이면 중복 X
        UserInfo userInfo =null;
        try {
            userInfo = usersProvider.retrieveUserInfoByID(userID);
        } catch (BaseException exception) {
            if (exception.getStatus() != NOT_FOUND_USER)
                throw exception;
        }
        if (userInfo != null)
            throw new BaseException(DUPLICATED_ID);
        else  return userID+"_valid";
    }


    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UserInfo existsEmail = null;
        UserInfo existsId = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsId = usersProvider.retrieveUserInfoByID(postUserReq.getId());
            if (existsId != null) log.info(existsId.toString());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsEmail = usersProvider.retrieveUserInfoByEmail(postUserReq.getEmail());
            if (existsEmail != null) log.info(existsEmail.toString());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsEmail != null || existsId != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String email = postUserReq.getEmail();
        String nickname = postUserReq.getNickname();
        String name = postUserReq.getName();
        String password;
        String id = postUserReq.getId();
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        UserInfo userInfo = new UserInfo(email,id, password, nickname, name);

        // 3. 유저 정보 저장
        try {
            usersMapper.save(userInfo);
            userInfo = usersMapper.findByIdx(userInfo.getUserIdx()).orElseGet(()->null);
            if (userInfo == null)
                throw new BaseException(FAILED_TO_POST_USER);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
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
    public PatchUserRes updateUserInfo(Long userIdx, PatchUserReq patchUserReq) throws BaseException {
        try {
            UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(()-> new BaseException(NOT_FOUND_USER));
            String email = patchUserReq.getEmail() + "_edited";
            String nickname = patchUserReq.getNickname()+"_edited";
            String name = patchUserReq.getName()+"_edited";
            String image = patchUserReq.getUserImage()+"_edited";
            log.info(userInfo.toString());

            if (patchUserReq.getEmail() != null && patchUserReq.getEmail().length() != 0)
                userInfo.setUserName(patchUserReq.getEmail());
            if (patchUserReq.getName() != null && patchUserReq.getName().length() != 0)
                userInfo.setUserName(patchUserReq.getName());
            if (patchUserReq.getNickname() != null && patchUserReq.getNickname().length() != 0)
                userInfo.setUserNickname(patchUserReq.getNickname());
            if (patchUserReq.getUserImage() != null && patchUserReq.getNickname().length() != 0)
                userInfo.setUserName(patchUserReq.getUserImage());

            usersMapper.update(userInfo);
            return new PatchUserRes(email, nickname, name,image);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /** 비밀번호 수정
     * @param userIdx, patchPWReq
     * @throws BaseException
     */
    public void editPW(Long userIdx, PatchPWReq patchPWReq) throws BaseException {
        UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_PASSWORD);
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
            throw new BaseException(FAILED_TO_PATCH_PASSWORD);
        }

        //변경사항 저장
        userInfo.setUserPassword(password);
        try {
            usersMapper.update(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_PASSWORD);
        }
    }
        /**
         * 회원 탈퇴
         * @param userIdx
         * @throws BaseException
         */
    public void deleteUserInfo(Long userIdx) throws BaseException {
        // 1. 존재하는 DTOUsers가 있는지 확인 후 저장
        UserInfo userInfo = usersProvider.retrieveUserInfoByUserIdx(userIdx);

        // 2-1. 해당 DTOUsers를 완전히 삭제
//        try {
//            usersMapper.delete(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 DTOUsers의 status를 WITHDRAWN로 설정
        userInfo.setUserStatus(WITHDRAWN.getValue());
        try {
            usersMapper.update(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }


}
