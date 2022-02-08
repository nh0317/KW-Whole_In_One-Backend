package com.naturemobility.seoul.service.partner;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.partners.*;
import com.naturemobility.seoul.domain.users.PatchPWReq;
import com.naturemobility.seoul.domain.users.PostLoginReq;
import com.naturemobility.seoul.domain.users.PostLoginRes;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletResponse;

public interface PartnerService {
    public String checkEmail(String partnerEmail) throws BaseException;
    public PostPartnerRes createPartnerInfo(PostPartnerReq postpartnerReq) throws BaseException;

    PostLoginRes login(HttpServletResponse response, PostLoginReq postLoginReq) throws BaseException, UsernameNotFoundException;

    PostLoginRes checkPW(HttpServletResponse response, String partnerEmail, String pw) throws BaseException;

    void updatePW(HttpServletResponse response, PartnerInfo partnerInfo, PatchPWReq patchPWReq) throws BaseException;

    public GetPartnerRes findPartnerInfo(Long partnerIdx) throws BaseException;
    public PatchPartnerRes updatePartnerInfo(PartnerInfo partnerInfo, PatchPartnerReq patchpartnerReq) throws BaseException;
    public void deletePartnerInfo(PartnerInfo partnerInfo) throws BaseException;

    PartnerInfo retrievePartnerInfoByPartnerIdx(Long partnerIdx) throws BaseException;

    PartnerInfo retrievePartnerInfoByEmail(String email) throws BaseException;
}
