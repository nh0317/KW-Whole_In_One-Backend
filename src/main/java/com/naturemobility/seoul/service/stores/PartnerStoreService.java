package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;

import com.naturemobility.seoul.domain.coupons.PostCouponReq;
import com.naturemobility.seoul.domain.partnerStore.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerStoreService {
    @Transactional
    PostPartnerStoreRes saveStore(PostStoreReq postStoreReq, Long partnerIdx) throws BaseException;

    GetPartnerStoreRes getStore(Long partnerIdx) throws BaseException;

    @Transactional
    PostStoreImageRes saveStoreImage(MultipartFile mainStoreImage, List<MultipartFile> storeImages, Long partnerIdx) throws BaseException;

    GetStoreIdxRes getStoreIdx(Long partnerIdx) throws BaseException;

    void postCoupon(Long partnerIdx, PostCouponReq postCouponReq) throws BaseException;
}
