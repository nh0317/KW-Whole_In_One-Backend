package com.naturemobility.seoul.service.partner;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.partners.*;
import com.naturemobility.seoul.domain.users.PatchPWReq;
import com.naturemobility.seoul.domain.users.PostLoginReq;
import com.naturemobility.seoul.domain.users.PostLoginRes;

public interface PartnerService {
    public String checkEmail(String partnerEmail) throws BaseException;
    public PostPartnerRes createPartnerInfo(PostPartnerReq postpartnerReq) throws BaseException;
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException;
    public PostLoginRes checkPW(String partnerEmail, String pw) throws BaseException;
    public void updatePW(PartnerInfo partnerInfo, PatchPWReq patchPWReq) throws BaseException;
    public GetPartnerRes findPartnerInfo(Long partnerIdx) throws BaseException;
    public PatchPartnerRes updatePartnerInfo(PartnerInfo partnerInfo, PatchPartnerReq patchpartnerReq) throws BaseException;
    public void deletePartnerInfo(PartnerInfo partnerInfo) throws BaseException;
    public PartnerInfo retrievePartnerInfoByPartnerIdx(Long partnerIdx) throws BaseException;
    public PartnerInfo retrievePartnerInfoByEmail(String email) throws BaseException;
}
