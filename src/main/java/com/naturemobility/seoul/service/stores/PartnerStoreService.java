package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.coupons.PostCouponReq;
import com.naturemobility.seoul.domain.stores.PostStoreReq;
import com.naturemobility.seoul.domain.stores.CommonStoreRes;

public interface PartnerStoreService {
    CommonStoreRes saveStore(PostStoreReq postStoreReq) throws BaseException;
    CommonStoreRes getStore(Long partnerIdx) throws BaseException;
    void postCoupon(Long partnerIdx, PostCouponReq postCouponReq) throws BaseException;
}
