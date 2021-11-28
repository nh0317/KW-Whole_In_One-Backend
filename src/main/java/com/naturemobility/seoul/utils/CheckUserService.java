package com.naturemobility.seoul.utils;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.CustomUserDetails;
import com.naturemobility.seoul.domain.partners.PartnerInfo;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.service.partner.PartnerService;
import com.naturemobility.seoul.service.users.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.naturemobility.seoul.config.BaseResponseStatus.NEED_LOGIN;


// TODO: 중복되는 부분 aop 적용하기
// 로그인한 유저의 정보가 필요한 경우
// 여기 있는 함수를 통해 user의 정보를 받아올 수 있다.
@Service
@Slf4j
public class CheckUserService {

    @Autowired
    UsersService usersService;

    @Autowired
    PartnerService partnerService;

    public String getEmail() throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            log.info(authentication.getPrincipal().toString());
            throw new BaseException(NEED_LOGIN);
        }
        else return  authentication.getName();
    }
    public UserInfo getUser() throws BaseException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            log.info(authentication.getPrincipal().toString());
//            throw new BaseException(NEED_LOGIN);
            UserInfo userInfo = usersService.retrieveUserInfoByUserIdx(1L);
            return userInfo;
        }
        else{
            UserInfo userInfo = usersService.retrieveUserInfoByEmail(authentication.getName());
            return userInfo;
        }
    }

    public PartnerInfo getPartner() throws BaseException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            log.info(authentication.getPrincipal().toString());
//            throw new BaseException(NEED_LOGIN);
//            CustomUserDetails details = (CustomUserDetails) authentication.getDetails();
            PartnerInfo partnerInfo = partnerService.retrievePartnerInfoByPartnerIdx(1L);
            return partnerInfo;
        }
        else{
            PartnerInfo partnerInfo = partnerService.retrievePartnerInfoByEmail(authentication.getName());
            return partnerInfo;
        }
    }

    public Long getUserIdx() throws BaseException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            log.info(authentication.getPrincipal().toString());
            return 1L;
//            throw new BaseException(NEED_LOGIN);
        }
        else{
            UserInfo userInfo = usersService.retrieveUserInfoByEmail(authentication.getName());
            return userInfo.getUserIdx();
        }
    }
    public Long getPartnerIdx() throws BaseException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            log.info(authentication.getPrincipal().toString());
            return 1L;
//            throw new BaseException(NEED_LOGIN);
        }
        else{
            PartnerInfo partnerInfo = partnerService.retrievePartnerInfoByEmail(authentication.getName());
            return partnerInfo.getPartnerIdx();
        }
    }


}
