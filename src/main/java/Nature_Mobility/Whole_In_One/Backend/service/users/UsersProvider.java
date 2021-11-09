package Nature_Mobility.Whole_In_One.Backend.service.users;

import Nature_Mobility.Whole_In_One.Backend.config.BaseException;
import Nature_Mobility.Whole_In_One.Backend.config.BaseResponse;
import Nature_Mobility.Whole_In_One.Backend.config.secret.Secret;
import Nature_Mobility.Whole_In_One.Backend.domain.users.*;
import Nature_Mobility.Whole_In_One.Backend.mapper.UsersMapper;
import Nature_Mobility.Whole_In_One.Backend.utils.AES128;
import Nature_Mobility.Whole_In_One.Backend.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Nature_Mobility.Whole_In_One.Backend.config.BaseResponseStatus.*;
import static Nature_Mobility.Whole_In_One.Backend.domain.UserStatus.*;
import static Nature_Mobility.Whole_In_One.Backend.utils.ValidationRegex.isRegexEmail;

@Service
@Slf4j
public class UsersProvider {
    private final UsersMapper usersMapper;
    private final JwtService jwtService;

    @Autowired
    public UsersProvider(UsersMapper usersMapper, JwtService jwtService) {
        this.usersMapper = usersMapper;
        this.jwtService = jwtService;
    }

    /**
     * 전체 회원 조회
     * @return List<GetUsersRes>
     * @throws BaseException
     */
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<DTOUsers> userInfoList;
        try {
            if (word == null) { // 전체 조회
                userInfoList = usersMapper.findByStatus(ACTIVE.getValue());
            } else { // 검색 조회
                userInfoList = usersMapper.findByStatusAndNicknameIsContaining(ACTIVE.getValue(), word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
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
    public GetUserRes retrieveUserInfo(Long userIdx) throws BaseException {
        // 1. DB에서 userIdx로 UserInfo 조회
        DTOUsers userInfo = retrieveUserInfoByUserIdx(userIdx);

        // 2. UserInfoRes로 변환하여 return
        Long idx = userInfo.getUserIdx();
        String id = userInfo.getUserId();
        String email = userInfo.getUserEmail();
        String nickname = userInfo.getUserNickname();
        String name = userInfo.getUserName();
        String image = userInfo.getUserImage();
        return new GetUserRes(idx,id, email, nickname, name, image);
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        DTOUsers userInfo = new DTOUsers();
        // 1. DB에서 UserInfo 조회
        String userId=postLoginReq.getId();
        if(userId.contains("@")){
             if (!isRegexEmail(userId))
                throw new BaseException(INVALID_EMAIL);
            else userInfo = retrieveUserInfoByEmail(userId);
        } else userInfo = retrieveUserInfoByID(userId);

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
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
    public PostLoginRes checkPW(Long userIdx, String pw) throws BaseException {
        DTOUsers userInfo = new DTOUsers();
        // 1. DB에서 UserInfo 조회
        userInfo = retrieveUserInfoByUserIdx(userIdx);

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getUserPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
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
    public DTOUsers retrieveUserInfoByUserIdx(Long userIdx) throws BaseException {
        // 1. DB에서 UserInfo 조회
        DTOUsers existsDTOUser;
        try {
            existsDTOUser = usersMapper.findByIdx(userIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 회원인지 확인
        DTOUsers userInfo=null;
        if (existsDTOUser != null) {
            if(existsDTOUser.getUserStatus().equals(ACTIVE.getValue()))
                userInfo=existsDTOUser;
            else if(existsDTOUser.getUserStatus().equals(INACTIVE.getValue()))
                throw new BaseException(INACTIVE_ID);
            else if(existsDTOUser.getUserStatus().equals(WITHDRAWN.getValue()))
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
    public DTOUsers retrieveUserInfoByEmail(String email) throws BaseException {
        List<DTOUsers> existsDTOUser = new ArrayList<>();
        DTOUsers userInfo = null;
        try {
            existsDTOUser = usersMapper.findByEmailAndStatus(email, ACTIVE.getValue());
        }
        catch (Exception exception) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        if (existsDTOUser.size()>0) userInfo = existsDTOUser.get(0);
        else {
            existsDTOUser = usersMapper.findByEmailAndStatus(email, INACTIVE.getValue());
            if (existsDTOUser.size() > 0) {
                log.info(existsDTOUser.size()+" 개");
                throw new BaseException(INACTIVE_ID);
            }
            else
                throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }
    /**
     * 회원 조회
     * @param id
     * @return DTOUsers
     * @throws BaseException
     */
    public DTOUsers retrieveUserInfoByID(String id) throws BaseException{
        List<DTOUsers> existsDTOUser = new ArrayList<>();
        DTOUsers userInfo = null;
        try {
            existsDTOUser = usersMapper.findByIdAndStatus(id, ACTIVE.getValue());
        }
        catch (Exception exception) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        if (existsDTOUser.size()>0) userInfo = existsDTOUser.get(0);
        if (existsDTOUser.size() == 0) {
            existsDTOUser = usersMapper.findByIdAndStatus(id, INACTIVE.getValue());
            if (existsDTOUser.size() > 0)
                throw new BaseException(INACTIVE_ID);
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
        DTOUsers user;
        Integer point=0, cntReservation=0, cntStoreLike=0, cntCoupon=0;
        String image=null, nickName=null;
        try{
            user = usersMapper.findByIdx(userIdx).orElseGet(()->null);
            if (user!=null) {
                nickName = user.getUserNickname();
                image = user.getUserImage();
                point = user.getUserPoint();
                cntReservation = usersMapper.cntReservation(userIdx).orElseGet(()->0);
                cntCoupon = usersMapper.cntCoupon(userIdx).orElseGet(()->0);
                log.info("쿠폰 수 : "+cntCoupon);
                cntStoreLike = usersMapper.cntStoreLike(userIdx).orElseGet(()->0);
                log.info("찜 매장 수 : "+cntStoreLike);
            }
        }
        catch (Exception exception){
            throw new BaseException(FAILED_TO_GET_USER);
        }
        GetMyPageRes getMyPageRes = new GetMyPageRes(image,nickName,cntReservation, cntStoreLike, point,cntCoupon);
        return getMyPageRes;
    }

}