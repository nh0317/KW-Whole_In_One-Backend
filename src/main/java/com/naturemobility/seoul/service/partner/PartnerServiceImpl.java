package com.naturemobility.seoul.service.partner;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.secret.Secret;
import com.naturemobility.seoul.domain.partners.*;
import com.naturemobility.seoul.domain.users.PatchPWReq;
import com.naturemobility.seoul.domain.users.PostLoginReq;
import com.naturemobility.seoul.domain.users.PostLoginRes;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.utils.AES128;
import com.naturemobility.seoul.utils.JwtService;
import com.naturemobility.seoul.utils.ValidationRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.config.BaseResponseStatus.NOT_FOUND_USER;

@Service
@Slf4j
public class PartnerServiceImpl implements PartnerService{

    @Autowired
    PartnerMapper partnerMapper;

    @Autowired
    JwtService jwtService;

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
        String password;
//        String id = postPartnerReq.getId();
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postPartnerReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }
        PartnerInfo partnerInfo = new PartnerInfo(email, password, name);

        // 3. 유저 정보 저장
        try{
            partnerMapper.save(partnerInfo);
        } catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }
        // 4. JWT 생성
        String jwt = jwtService.createPartnerJwt(partnerInfo.getPartnerIdx());

        // 5. DTOPartnersLoginRes로 변환하여 return
        Long idx = partnerInfo.getPartnerIdx();
        return new PostPartnerRes(idx, jwt);
    }

    /** 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        PartnerInfo partnerInfo;
        // 1. DB에서 PartnerInfo 조회
        String partnerId=postLoginReq.getId();
        if (!ValidationRegex.isRegexEmail(partnerId))
            throw new BaseException(INVALID_EMAIL);
        else partnerInfo = retrievePartnerInfoByEmail(partnerId);

        // 2. PartnerInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(partnerInfo.getPartnerPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!postLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createPartnerJwt(partnerInfo.getPartnerIdx());

        // 4. PostLoginRes 변환하여 return
        Long idx = partnerInfo.getPartnerIdx();
        return new PostLoginRes(idx, jwt);
    }

    /** 비밀번호 확인
     * @param partnerIdx, pw
     * @return PostLoginRes
     * @throws BaseException
     */
    @Override
    public PostLoginRes checkPW(Long partnerIdx, String pw) throws BaseException {
        PartnerInfo partnerInfo;
        // 1. DB에서 PartnerInfo 조회
        partnerInfo = retrievePartnerInfoByPartnerIdx(partnerIdx);

        // 2. PartnerInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(partnerInfo.getPartnerPassword());
        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!pw.equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createPartnerJwt(partnerInfo.getPartnerIdx());

        // 4. PostLoginRes 변환하여 return
        Long idx = partnerInfo.getPartnerIdx();
        return new PostLoginRes(idx, jwt);
    }
    /** 비밀번호 수정
     * @param partnerIdx, patchPWReq
     * @throws BaseException
     */
    @Override
    public void updatePW(Long partnerIdx, PatchPWReq patchPWReq) throws BaseException {

        PartnerInfo partnerInfo = partnerMapper.findByIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(partnerInfo.getPartnerPassword());
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
     * @param partnerIdx, patchPartnerReq
     * @return PatchPartnerRes
     * @throws BaseException
     */
    @Override
    public PatchPartnerRes updatePartnerInfo(Long partnerIdx, PatchPartnerReq patchPartnerReq) throws BaseException {
        try {
            PartnerInfo partnerInfo = partnerMapper.findByIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_USER));
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
     * @param partnerIdx
     * @throws BaseException
     */
    @Override
    public void deletePartnerInfo(Long partnerIdx) throws BaseException {
        // 1. 존재하는 DTOPartners가 있는지 확인 후 저장
        PartnerInfo partnerInfo = retrievePartnerInfoByPartnerIdx(partnerIdx);

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
}
