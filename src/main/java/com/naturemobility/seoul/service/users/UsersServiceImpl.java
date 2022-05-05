package com.naturemobility.seoul.service.users;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.config.secret.Secret;
import com.naturemobility.seoul.domain.users.*;
import com.naturemobility.seoul.jwt.JwtFilter;
import com.naturemobility.seoul.jwt.JwtService;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.domain.users.UserStatus;
import com.naturemobility.seoul.redis.RedisService;
import com.naturemobility.seoul.service.s3.FileUploadService;
import com.naturemobility.seoul.utils.CookieUtil;
import com.naturemobility.seoul.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.jwt.JwtFilter.AUTHORIZATION_HEADER;


@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecretPropertyConfig secretPropertyConfig;
    private final CookieUtil cookieUtil;
    private final RedisService redisService;
    private final FileUploadService fileUploadService;

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
        UserInfo userInfo = new UserInfo(postUserReq.getEmail(), passwordEncoder.encode(postUserReq.getPassword()),
                postUserReq.getNickname(), postUserReq.getName(), postUserReq.getTel());
        // 3. 유저 정보 저장
        usersMapper.save(userInfo);

        // 5. DTOUsersLoginRes로 변환하여 return
        Long idx = userInfo.getUserIdx();
        return new PostUserRes(idx);
    }

    /** 회원 정보 수정
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    @Override
    public PatchUserRes updateUserInfo(UserInfo userInfo, PatchUserReq patchUserReq) throws BaseException {
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
    }

    /** 비밀번호 수정
     * @param res, userInfo, patchPWReq
     * @throws BaseException
     */
    @Override
    public void updatePW(HttpServletResponse res, UserInfo userInfo, PatchPWReq patchPWReq) throws BaseException {
        log.info("비밀번호 변경 " + userInfo.getUserEmail());
        createJwt(res,userInfo.getUserEmail(), patchPWReq.getPassword());
        //새로운 비밀번호 일치 확인
        if (!patchPWReq.getNewPassword().equals(patchPWReq.getConfirmNewPassword())) {
            throw new BaseException(WRONG_PASSWORD);
        }

        //암호화
        String password = passwordEncoder.encode(patchPWReq.getNewPassword());

        //변경사항 저장
        userInfo.setUserPassword(password);
        usersMapper.update(userInfo);
    }
        /**
         * 회원 탈퇴
         * @param userInfo
         * @throws BaseException
         */
    @Override
    public void deleteUserInfo(UserInfo userInfo) throws BaseException {

        // 2-1. 해당 DTOUsers를 완전히 삭제
//        try {
//            usersMapper.delete(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 DTOUsers의 status를 WITHDRAWN로 설정
        userInfo.setUserStatus(UserStatus.WITHDRAWN.getValue());
        usersMapper.update(userInfo);
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
        if (word == null) // 전체 조회
            userInfoList = usersMapper.findByStatus(UserStatus.ACTIVE.getValue());
         else // 검색 조회
            userInfoList = usersMapper.findByStatusAndNicknameIsContaining(UserStatus.ACTIVE.getValue(), word);

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            Long idx = userInfo.getUserIdx();
            String email = userInfo.getUserEmail();
            return new GetUsersRes(idx, email);
        }).collect(Collectors.toList());
    }

    @Override
    public PostUserImageRes postUserImage(MultipartFile userImage, Long userIdx) throws BaseException{
        String filepath = fileUploadService.uploadImage(userImage);
        UserInfo userInfo = retrieveUserInfoByUserIdx(userIdx);
        userInfo.setUserImage(filepath);
        usersMapper.update(userInfo);
        return new PostUserImageRes(filepath);

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
        return new GetUserRes(userInfo.getUserIdx(), userInfo.getUserEmail(), userInfo.getUserNickname(),
                userInfo.getUserImage(), userInfo.getUserImage());
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes login(HttpServletResponse response, PostLoginReq postLoginReq) throws BaseException {
        // 1. DB에서 UserInfo 조회
        String userId=postLoginReq.getId();
        if (!ValidationRegex.isRegexEmail(userId))
            throw new BaseException(INVALID_EMAIL);
        else retrieveUserInfoByEmail(userId);

        // 4. Create JWT
        return createJwt(response,postLoginReq.getId(), postLoginReq.getPassword());
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res) throws BaseException{
        Cookie refreshToken = cookieUtil.getCookie(req, JwtService.REFRESH_TOKEN);
        String refreshJwt=null;
        if(refreshToken!=null){
            refreshJwt = refreshToken.getValue();
        }
        if (refreshJwt!=null && redisService.isExist(refreshJwt)){
            redisService.deleteValues(refreshJwt);
            jwtService.expireTokens(res,refreshToken);
        } else throw new BaseException(NEED_LOGIN);
    }
    /**
     * 비밀번호 확인
     * @param userEmail, pw
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes checkPW(HttpServletResponse response,String userEmail, String pw) throws BaseException {
        return createJwt(response, userEmail, pw);
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
    @Override
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException {
        List<UserInfo> existsDTOUser = new ArrayList<>();
        UserInfo userInfo = null;
        log.info(email + UserStatus.ACTIVE.getValue().toString());
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

    @Override
    public PostLoginRes refreshToken(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Cookie refreshToken = cookieUtil.getCookie(request, JwtService.REFRESH_TOKEN);
        String refreshJwt=null;
//        log.info(refreshToken.toString()," ");
        if(refreshToken!=null){
            refreshJwt = refreshToken.getValue();
//            log.info(refreshJwt);
        }
        if (refreshJwt!=null && redisService.isExist(refreshJwt)){
            Authentication authentication = jwtService.getAuthenticationFromRefreshToken(refreshJwt);
            String newJwt = jwtService.createJwt(authentication);
//            String newRefreshJwt = jwtService.createRefreshJwt(authentication);
            jwtService.setTokens(response,authentication,newJwt,refreshJwt);
//            redisService.deleteValues(refreshJwt);
            return new PostLoginRes(authentication.getName(),newJwt, refreshJwt, Long.parseLong(secretPropertyConfig.getTokenValidityInSeconds()));
        } else throw new BaseException(NEED_LOGIN);
    }

    @Override
    public void postCoupon(Long userIdx, Long couponIdx) throws BaseException {
        if (usersMapper.postCouponCheck(userIdx,couponIdx)!=0){
            throw new BaseException(NO_AUTHORITY);
        }
        
        PostUserCoupon postUserCoupon = new PostUserCoupon(userIdx,couponIdx);
        usersMapper.postCoupon(postUserCoupon);
        return;
    }

    @Override
    public List<GetUserCoupon> getUserCoupon(Long userIdx) throws BaseException {
        List<GetUserCoupon> getUserCoupons = usersMapper.getUserCouponsList(userIdx);
        return getUserCoupons;
    }

    /** 마이페이지
     * @param userIdx
     * @return GetMyPageRes
     * @throws BaseException
     */
    @Override
    public GetMyPageRes myPage(Long userIdx) throws BaseException {
        log.info("user {}", userIdx);
        UserInfo user = usersMapper.findByIdx(userIdx).orElseThrow(()-> new BaseException(NOT_FOUND_USER));
        return new GetMyPageRes(user.getUserImage(),user.getUserNickname(),
                usersMapper.cntReservation(userIdx).orElseGet(()->0), usersMapper.cntStoreLike(userIdx).orElseGet(()->0),
                user.getUserPoint(),usersMapper.cntCoupon(userIdx).orElseGet(()->0));
    }

    private PostLoginRes createJwt(HttpServletResponse res, String email, String pw) throws BaseException {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email+":user", pw);
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        }catch ( Exception exception){
            throw new BaseException(WRONG_PASSWORD);
        }
        String jwt = jwtService.createJwt(authentication);
        String refreshJwt = jwtService.createRefreshJwt(authentication);
        jwtService.setTokens(res, authentication, jwt, refreshJwt);
        return new PostLoginRes(email, jwt, refreshJwt, Long.parseLong(secretPropertyConfig.getTokenValidityInSeconds()));
    }


}
