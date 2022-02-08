package com.naturemobility.seoul.service.partner;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.config.secret.Secret;
import com.naturemobility.seoul.domain.partners.*;
import com.naturemobility.seoul.domain.users.PatchPWReq;
import com.naturemobility.seoul.domain.users.PostLoginReq;
import com.naturemobility.seoul.domain.users.PostLoginRes;
import com.naturemobility.seoul.jwt.JwtFilter;
import com.naturemobility.seoul.jwt.JwtService;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.utils.ValidationRegex;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.config.BaseResponseStatus.NOT_FOUND_USER;

@Service
@Slf4j
@AllArgsConstructor
public class PartnerServiceImpl implements PartnerService{

    private final PartnerMapper partnerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecretPropertyConfig secretPropertyConfig;

    /**
     * 중복 회원 검증
     * @param partnerEmail
     * @return String
     * @throws BaseException
     */
    @Override
    public String checkEmail(String partnerEmail) throws BaseException {
        //false 이면 중복, true 이면 중복 X
        PartnerInfo partnerInfo =null;
        try {
            partnerInfo = retrievePartnerInfoByEmail(partnerEmail);
            log.info(partnerInfo.toString());
        } catch (BaseException exception) {
            log.info(exception.getStatus().toString());
            if (exception.getStatus() != NOT_FOUND_USER)
                throw exception;
        }
        if (partnerInfo != null)
            throw new BaseException(DUPLICATED_EMAIL);
        else  return partnerEmail+"_valid";
    }

    /**
     * 회원가입
     * @param postPartnerReq
     * @return PostPartnerRes
     * @throws BaseException
     */
    @Override
    public PostPartnerRes createPartnerInfo(PostPartnerReq postPartnerReq) throws BaseException {
        PartnerInfo existsEmail = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsEmail = retrievePartnerInfoByEmail(postPartnerReq.getEmail());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsEmail != null)
            throw new BaseException(DUPLICATED_USER);

        // 2. 유저 정보 생성
        String email = postPartnerReq.getEmail();
        String name = postPartnerReq.getName();
        String password = passwordEncoder.encode(postPartnerReq.getPassword());
        PartnerInfo partnerInfo = new PartnerInfo(email, password, name);

        // 3. 유저 정보 저장
        try{
            partnerMapper.save(partnerInfo);
        } catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 4. DTOPartnersLoginRes로 변환하여 return
        Long idx = partnerInfo.getPartnerIdx();
        return new PostPartnerRes(idx);
    }

    /** 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes login(HttpServletResponse response,PostLoginReq postLoginReq) throws BaseException, UsernameNotFoundException {
        PartnerInfo partnerInfo;
        // 1. DB에서 PartnerInfo 조회
        String partnerId=postLoginReq.getId();
        if (!ValidationRegex.isRegexEmail(partnerId))
            throw new BaseException(INVALID_EMAIL);
        else retrievePartnerInfoByEmail(partnerId);

        // 2.유저 조회 및 비밀 번호 확인
        // 4. PostLoginRes 변환하여 return
        return createJwt(response,postLoginReq.getId(), postLoginReq.getPassword());
    }

    /** 비밀번호 확인
     * @param partnerEmail, pw
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes checkPW(HttpServletResponse response,String partnerEmail, String pw) throws BaseException {

        return createJwt(response,partnerEmail, pw);
    }
    /** 비밀번호 수정
     * @param partnerInfo, patchPWReq
     * @throws BaseException
     */
    @Override
    public void updatePW(HttpServletResponse response,PartnerInfo partnerInfo, PatchPWReq patchPWReq) throws BaseException {

        // 3. Create JWT
        createJwt(response,partnerInfo.getPartnerEmail(), patchPWReq.getPassword());

        //새로운 비밀번호 일치 확인
        if (!patchPWReq.getNewPassword().equals(patchPWReq.getConfirmNewPassword())) {
            throw new BaseException(WRONG_PASSWORD);
        }

        //암호화
        String password = passwordEncoder.encode(patchPWReq.getNewPassword());

        //변경사항 저장
        partnerInfo.setPartnerPassword(password);
        try {
            partnerMapper.update(partnerInfo);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

    }

    /**
     * 회원 조회
     * @param partnerIdx
     * @return GetPartnerRes
     * @throws BaseException
     */
    @Override
    public GetPartnerRes findPartnerInfo(Long partnerIdx) throws BaseException {
        // 1. DB에서 partnerIdx로 PartnerInfo 조회
        PartnerInfo partnerInfo = retrievePartnerInfoByPartnerIdx(partnerIdx);

        // 2. PartnerInfoRes로 변환하여 return
        Long idx = partnerInfo.getPartnerIdx();
        String email = partnerInfo.getPartnerEmail();
        String name = partnerInfo.getPartnerName();
        return new GetPartnerRes(idx, email, name);
    }
    /**
     * 회원 수정
     * @param partnerInfo, patchPartnerReq
     * @return PatchPartnerRes
     * @throws BaseException
     */
    @Override
    public PatchPartnerRes updatePartnerInfo(PartnerInfo partnerInfo, PatchPartnerReq patchPartnerReq) throws BaseException {
        try {
            String name = patchPartnerReq.getName()+"_edited";

            if (patchPartnerReq.getName() != null && patchPartnerReq.getName().length() != 0)
                partnerInfo.setPartnerName(patchPartnerReq.getName());

            partnerMapper.update(partnerInfo);
            return new PatchPartnerRes(name);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
    /**
     * 회원 탈퇴
     * @param partnerInfo
     * @throws BaseException
     */
    @Override
    public void deletePartnerInfo(PartnerInfo partnerInfo) throws BaseException {
        // 1. 존재하는 DTOPartners가 있는지 확인 후 저장

        // 2-1. 해당 DTOPartners를 완전히 삭제
//        try {
//            partnersMapper.delete(partnerInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 DTOPartners의 status를 WITHDRAWN로 설정
        partnerInfo.setPartnerStatus(PartnerStatus.WITHDRAWN.getValue());
        try {
            partnerMapper.update(partnerInfo);
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
    /**
     * 회원 조회
     * @param partnerIdx
     * @return PartnerInfo
     * @throws BaseException
     */
    @Override
    public PartnerInfo retrievePartnerInfoByPartnerIdx(Long partnerIdx) throws BaseException {
        // 1. DB에서 PartnerInfo 조회
        PartnerInfo existsDTOPartner;
        existsDTOPartner = partnerMapper.findByIdx(partnerIdx).orElseThrow(()->new BaseException(RESPONSE_ERROR));

        // 2. 존재하는 회원인지 확인
        PartnerInfo partnerInfo =null;
        if (existsDTOPartner != null) {
            if(existsDTOPartner.getPartnerStatus().equals(PartnerStatus.ACTIVE.getValue()))
                partnerInfo =existsDTOPartner;
            else if(existsDTOPartner.getPartnerStatus().equals(PartnerStatus.INACTIVE.getValue()))
                throw new BaseException(INACTIVE_ID);
            else if(existsDTOPartner.getPartnerStatus().equals(PartnerStatus.WITHDRAWN.getValue()))
                throw new BaseException(NOT_FOUND_USER);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. PartnerInfo를 return
        return partnerInfo;
    }
    /**
     * 회원 조회
     * @param email
     * @return PartnerInfo
     * @throws BaseException
     */
    @Override
    public PartnerInfo retrievePartnerInfoByEmail(String email) throws BaseException {
        List<PartnerInfo> existsDTOPartner = new ArrayList<>();
        PartnerInfo partnerInfo = null;
        existsDTOPartner = partnerMapper.findByEmailAndStatus(email, PartnerStatus.ACTIVE.getValue());

        if (existsDTOPartner.size()>0) partnerInfo = existsDTOPartner.get(0);
        else {
            existsDTOPartner = partnerMapper.findByEmailAndStatus(email, PartnerStatus.INACTIVE.getValue());
            if (existsDTOPartner.size() > 0) {
                throw new BaseException(INACTIVE_ID);
            }
            else
                throw new BaseException(NOT_FOUND_USER);
        }
        return partnerInfo;
    }

    private PostLoginRes createJwt(HttpServletResponse response, String email, String pw) throws BaseException {
        UsernamePasswordAuthenticationToken authenticationToken;
        try {
            authenticationToken
                    = new UsernamePasswordAuthenticationToken(email+":partner", pw);
        }catch ( UsernameNotFoundException exception){
            throw new BaseException(WRONG_PASSWORD);
        }
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.createJwt(authentication);
        String refreshJwt = jwtService.createRefreshJwt(authentication);
        jwtService.setTokens(response, authentication, jwt, refreshJwt);
        return new PostLoginRes(email, jwt, refreshJwt, Long.parseLong(secretPropertyConfig.getRefreshTokenValidityInSeconds()));
    }
}
