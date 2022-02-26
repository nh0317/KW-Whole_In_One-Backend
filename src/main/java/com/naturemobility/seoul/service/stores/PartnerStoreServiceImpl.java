package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.brand.BrandInfo;
import com.naturemobility.seoul.domain.partnerStore.PostPartnerStoreRes;
import com.naturemobility.seoul.domain.partnerStore.PostStoreImageRes;
import com.naturemobility.seoul.domain.partnerStore.PostStoreReq;
import com.naturemobility.seoul.domain.partnerStore.GetPartnerStoreRes;
import com.naturemobility.seoul.domain.storeImage.StoreImageFileInfo;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.mapper.BrandMapper;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.PartnerStoreMapper;
import com.naturemobility.seoul.mapper.StoreImageFileMapper;
import com.naturemobility.seoul.service.s3.FileUploadService;
import com.naturemobility.seoul.utils.CheckUserService;
import com.naturemobility.seoul.utils.GeocoderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Service
@Slf4j
public class PartnerStoreServiceImpl implements PartnerStoreService{
    @Autowired
    PartnerStoreMapper partnerStoreMapper;
    @Autowired
    StoreImageFileMapper storeImageFileMapper;
    @Autowired
    GeocoderService geocoderService;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    BrandMapper brandMapper;
    @Autowired
    FileUploadService fileUploadService;

    @Override
    @Transactional
    public PostPartnerStoreRes saveStore(PostStoreReq postStoreReq, Long partnerIdx) throws BaseException {
        Map<String, Double> coords = geocoderService.getGeoDataByAddress(postStoreReq.getStoreLocation());
        if(coords==null){
            throw new BaseException(REQUEST_ERROR);
        }
        StoreInfo storeInfo = new StoreInfo(postStoreReq,coords.get("latitude"),coords.get("longitude"));
        if(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).isPresent()){
            storeInfo.setStoreBrand(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA)));
        }
        else if (postStoreReq.getStoreBrand().equals("미설정")|| postStoreReq.getStoreBrand()==null || postStoreReq.getStoreBrand().equals(""))
            storeInfo.setStoreBrand(null);
        else {
            BrandInfo brandInfo = new BrandInfo(postStoreReq.getStoreBrand());
            brandMapper.save(brandInfo);
            storeInfo.setStoreBrand(brandInfo.getBrandIdx());
        }
        if(partnerMapper.findStoreIdx(partnerIdx).isPresent()) {
            storeInfo.setStoreIdx(partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA)));
            partnerStoreMapper.update(storeInfo);
        }
        else {
            partnerStoreMapper.save(storeInfo);
            partnerMapper.saveStoreIdx(partnerIdx, storeInfo.getStoreIdx());
        }

        return new PostPartnerStoreRes(storeInfo,postStoreReq.getStoreBrand());
    }

    @Override
    public GetPartnerStoreRes getStore(Long partnerIdx) throws BaseException {
        try{
            if (partnerMapper.findStoreIdx(partnerIdx).isPresent()) {
                Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
                StoreInfo storeInfo = partnerStoreMapper.findByStoreIdx(storeIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
                List<String> storeImages = storeImageFileMapper.findByStoreIdx(storeIdx);

                if (storeInfo.getStoreImage() == null || storeInfo.getStoreImage().length() == 0)
                    storeInfo.setStoreInfo("https://i.ibb.co/9hL4Cxk/default-image.jpg");

                String storeBrand = "";
                if (storeInfo.getStoreBrand() == null)
                    storeBrand = "미 설정";
                else storeBrand = storeInfo.getBrandName();
                return new GetPartnerStoreRes(storeInfo, storeBrand, storeImages);

            } else {
                return new GetPartnerStoreRes(new StoreInfo(), null, new ArrayList<>());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public PostStoreImageRes saveStoreImage(MultipartFile mainStoreImage, List<MultipartFile> storeImages, Long partnerIdx)
            throws BaseException {
        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String mainImgPath = fileUploadService.uploadImage(mainStoreImage);
        partnerStoreMapper.updateStoreImage(storeIdx, mainImgPath);
        List<String> images = new ArrayList<>();
        for (MultipartFile img : storeImages) {
            String filepath = fileUploadService.uploadImage(img);
            storeImageFileMapper.save(new StoreImageFileInfo(storeIdx, filepath));
            images.add(filepath);
        }
        return new PostStoreImageRes(mainImgPath, images);
    }
}
