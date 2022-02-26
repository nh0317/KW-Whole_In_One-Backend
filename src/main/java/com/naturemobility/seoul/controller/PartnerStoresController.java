package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.partnerStore.PostPartnerStoreRes;
import com.naturemobility.seoul.domain.partnerStore.PostStoreImageRes;
import com.naturemobility.seoul.domain.partnerStore.PostStoreReq;
import com.naturemobility.seoul.domain.partnerStore.GetPartnerStoreRes;
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
    public BaseResponse<PostStoreImageRes> registerStoreImages(@RequestPart("mainStoreImage") MultipartFile mainStoreImage,
                                                                 @RequestPart("storeImages") List<MultipartFile> storeImages )
            throws BaseException{
        Long partnerIdx = checkUserService.getPartnerIdx();
        PostStoreImageRes getPartnerStoreRes = partnerStoreService.saveStoreImage(mainStoreImage,storeImages,partnerIdx);
        return new BaseResponse<>(SUCCESS, getPartnerStoreRes);
    }

}

