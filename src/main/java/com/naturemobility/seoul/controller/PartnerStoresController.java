package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;

import com.naturemobility.seoul.domain.coupons.PostCouponReq;
import com.naturemobility.seoul.domain.partnerStore.*;
import com.naturemobility.seoul.service.s3.FileUploadService;

import com.naturemobility.seoul.service.stores.PartnerStoreService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("partner")
@Slf4j
public class PartnerStoresController {

    @Autowired
    PartnerStoreService partnerStoreService;
    @Autowired
    CheckUserService checkUserService;

    @GetMapping("/myStore")
    public BaseResponse<GetPartnerStoreRes> getStore() throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        GetPartnerStoreRes getPartnerStoreRes = partnerStoreService.getStore(partnerIdx);
//            log.info(commonStoreRes.get);
        return new BaseResponse<>(SUCCESS, getPartnerStoreRes);
    }

    @PostMapping("/register")
    public BaseResponse<PostPartnerStoreRes> registerStore(@RequestBody PostStoreReq postStoreReq) throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostPartnerStoreRes result = partnerStoreService.saveStore(postStoreReq, partnerIdx);
        return new BaseResponse<>(SUCCESS, result);
    }
    @PostMapping("/register_image")
    public BaseResponse<PostStoreImageRes> registerStoreImages(@RequestPart(value = "mainStoreImage", required = false) MultipartFile mainStoreImage,
                                                                 @RequestPart(value = "storeImages", required = false) List<MultipartFile> storeImages )
            throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostStoreImageRes getPartnerStoreRes = partnerStoreService.saveStoreImage(mainStoreImage,storeImages,partnerIdx);
        return new BaseResponse<>(SUCCESS, getPartnerStoreRes);
    }

    @GetMapping("/get_storeIdx")
    public BaseResponse<GetStoreIdxRes> getStoreIdx()
            throws BaseException{
        return new BaseResponse<>(SUCCESS, partnerStoreService.getStoreIdx(checkUserService.getPartnerIdx()));
    }
    /**
     * 쿠폰등록 API
     * [POST] /partner/coupons
     * @RequestBody
     * String couponName
     * Integer couponPercentage
     * String couponDeadline
     * @return BaseResponse
     */

    @PostMapping("/coupon")
    public BaseResponse postCouponInfo(@RequestBody PostCouponReq postCouponReq) throws BaseException{

        Long partnerIdx = checkUserService.getPartnerIdx();
        partnerStoreService.postCoupon(partnerIdx, postCouponReq);
        return new BaseResponse<>(SUCCESS);
    }
}

