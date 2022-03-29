package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.brand.BrandInfo;
import com.naturemobility.seoul.domain.coupons.PostCouponReq;
import com.naturemobility.seoul.domain.coupons.PostCouponInfo;

import com.naturemobility.seoul.domain.partnerStore.*;
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
public class PartnerStoreServiceImpl implements PartnerStoreService {
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
        if (coords == null) {
            throw new BaseException(REQUEST_ERROR);
        }
        StoreInfo storeInfo = new StoreInfo(postStoreReq, coords.get("latitude"), coords.get("longitude"));
        if (brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).isPresent()) {
            storeInfo.setStoreBrand(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA)));
        } else if (postStoreReq.getStoreBrand().equals("미설정") || postStoreReq.getStoreBrand() == null || postStoreReq.getStoreBrand().equals(""))
            storeInfo.setStoreBrand(null);
        else {
            BrandInfo brandInfo = new BrandInfo(postStoreReq.getStoreBrand());
            brandMapper.save(brandInfo);
            storeInfo.setStoreBrand(brandInfo.getBrandIdx());
        }
        if (partnerMapper.findStoreIdx(partnerIdx).isPresent()) {
            storeInfo.setStoreIdx(partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA)));
            partnerStoreMapper.update(storeInfo);
        } else {
            partnerStoreMapper.save(storeInfo);
            partnerMapper.saveStoreIdx(partnerIdx, storeInfo.getStoreIdx());
        }

        return new PostPartnerStoreRes(storeInfo, postStoreReq.getStoreBrand());
    }

    @Override
    public GetPartnerStoreRes getStore(Long partnerIdx) throws BaseException {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public PostStoreImageRes saveStoreImage(MultipartFile mainStoreImage, List<MultipartFile> storeImages, Long partnerIdx)
            throws BaseException {
        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String mainImgPath = "";
        List<String> images = new ArrayList<>();
        if (mainStoreImage != null && !mainStoreImage.equals("")) {
            mainImgPath = fileUploadService.uploadImage(mainStoreImage);
            partnerStoreMapper.updateStoreImage(storeIdx, mainImgPath);
        }
        if (storeImages != null && !storeImages.isEmpty()) {
            for (MultipartFile img : storeImages) {
                String filepath = fileUploadService.uploadImage(img);
                storeImageFileMapper.save(new StoreImageFileInfo(storeIdx, filepath));
                images.add(filepath);
            }
        }
        return new PostStoreImageRes(mainImgPath, images);
    }

    @Override
    public GetStoreIdxRes getStoreIdx(Long partnerIdx) throws BaseException {
        return new GetStoreIdxRes(partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA)));
    }

    @Override
    public void postCoupon(Long partnerIdx, PostCouponReq postCouponReq) throws BaseException {

        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        ;
        PostCouponInfo postcouponInfo = new PostCouponInfo(storeIdx, postCouponReq.getCouponName(),
                postCouponReq.getCouponPercentage(), postCouponReq.getCouponDeadline());
        partnerStoreMapper.postCouponInfo(postcouponInfo);
        return;
    }

    @Override
    public void deleteCoupon(Long partnerIdx, Long couponIdx) throws BaseException {

        Long storeIdxFromCouponIdx = partnerStoreMapper.getStoreIdxByCouponIdx(couponIdx);
        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));

        if (storeIdxFromCouponIdx != storeIdx) {
            throw new BaseException(NO_AUTHORITY);
        }
        partnerStoreMapper.deleteCouponInfo(couponIdx);
        return;
    }

    @Override
    public void postRoomInfo(Long partnerIdx, PostRoomInfoReq postRoomInfoReq) throws BaseException {

        PostRoomInfo postRoomInfo = new PostRoomInfo(postRoomInfoReq.getRoomName(), partnerIdx);
        partnerStoreMapper.postRoomInfo(postRoomInfo);
        return;
    }

    @Override
    public void deleteRoom(Long partnerIdx, Long roomIdx) throws BaseException {

        Long partnerIdxByRoomIdx = partnerStoreMapper.getPartnerIdxByRoomIdx(roomIdx);
        if (partnerIdx != partnerIdxByRoomIdx) {
            throw new BaseException(NO_AUTHORITY);
        }
        partnerStoreMapper.deleteRoom(roomIdx);
    }

    @Override
    public void deleteStoreImg(Long partnerIdx, Long imgFileIdx) throws BaseException {

        Long storeIdxFromImgFileIdx = partnerStoreMapper.getStoreIdxByImgFileIdx(imgFileIdx);
        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        if (storeIdx != storeIdxFromImgFileIdx) {
            throw new BaseException(NO_AUTHORITY);
        }
        partnerStoreMapper.deleteImgFile(imgFileIdx);
        return;
    }
}
