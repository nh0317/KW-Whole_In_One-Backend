package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.PostStoreReq;
import com.naturemobility.seoul.domain.stores.CommonStoreRes;
import com.naturemobility.seoul.mapper.PartnerStoreMapper;
import com.naturemobility.seoul.service.stores.PartnerStoreService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<CommonStoreRes> getStore() throws BaseException{
        CommonStoreRes commonStoreRes;
        Long partnerIdx = checkUserService.getPartnerIdx();
        commonStoreRes = partnerStoreService.getStore(partnerIdx);
//            log.info(commonStoreRes.get);
        return new BaseResponse<>(SUCCESS, commonStoreRes);
    }

    @PostMapping("/register")
    public BaseResponse<CommonStoreRes> registerStore(@RequestBody PostStoreReq postStoreReq) throws BaseException{
        CommonStoreRes commonStoreRes;
        commonStoreRes = partnerStoreService.saveStore(postStoreReq);
        return new BaseResponse<>(SUCCESS, commonStoreRes);
    }
}
