package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.brand.BrandInfo;
import com.naturemobility.seoul.domain.coupons.PostCouponReq;
import com.naturemobility.seoul.domain.coupons.PostCouponInfo;
import com.naturemobility.seoul.domain.stores.PostStoreReq;
import com.naturemobility.seoul.domain.stores.CommonStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.mapper.BrandMapper;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.PartnerStoreMapper;
import com.naturemobility.seoul.mapper.StoreImageFileMapper;
import com.naturemobility.seoul.utils.CheckUserService;
import com.naturemobility.seoul.utils.GeocoderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.naturemobility.seoul.config.BaseResponseStatus.NOT_FOUND_DATA;
import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;
import static java.lang.Boolean.FALSE;

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
    CheckUserService checkUserService;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    BrandMapper brandMapper;

    @Override
    @Transactional
    public CommonStoreRes saveStore(PostStoreReq postStoreReq) throws BaseException {
        CommonStoreRes commonStoreRes;
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreName(postStoreReq.getStoreName());
        storeInfo.setStoreInfo(postStoreReq.getStoreInfo());
        storeInfo.setStorePhoneNumber(postStoreReq.getStorePhoneNumber());
        storeInfo.setStoreLocation(postStoreReq.getStoreLocation());
        storeInfo.setStoreTime(postStoreReq.getStoreTime());
        storeInfo.setStoreImage(postStoreReq.getMainStoreImage());
        storeInfo.setBatCount(postStoreReq.getBatCount());
        storeInfo.setLefthandStatus(postStoreReq.getLefthandStatus());
        storeInfo.setParkingStatus(postStoreReq.getParkingStatus());
        storeInfo.setGroupSeatStatus(postStoreReq.getGroupSeatStatus());
        storeInfo.setFloorScreenStatus(postStoreReq.getFloorScreenStatus());
        storeInfo.setStorageStatus(postStoreReq.getStorageStatus());
        storeInfo.setLessonStatus(postStoreReq.getLessonStatus());
        storeInfo.setReserveStatus(postStoreReq.getReserveStatus());
        storeInfo.setCouponStatus(postStoreReq.getCouponStatus());
        //TODO: storeImage 테이블에 저장하는 로직 추가
        if(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).isPresent()){
            storeInfo.setStoreBrand(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA)));
        }
        else if (postStoreReq.getStoreBrand().equals("미설정")|| postStoreReq.getStoreBrand()==null || postStoreReq.getStoreBrand().equals(""))
            storeInfo.setStoreBrand(null);
        else {
            BrandInfo brandInfo = new BrandInfo();
            brandInfo.setBrandName(postStoreReq.getStoreBrand());
            brandMapper.save(brandInfo);
            storeInfo.setStoreBrand(brandInfo.getBrandIdx());
        }
        Map<String, Double> coords = geocoderService.getGeoDataByAddress(postStoreReq.getStoreLocation());
        if(coords==null){
            throw new BaseException(RESPONSE_ERROR);
        }
        storeInfo.setStoreLatitude(coords.get("latitude"));
        storeInfo.setStoreLongitude(coords.get("longitude"));

        Long partnerIdx = checkUserService.getPartnerIdx();
        if(partnerMapper.findStoreIdx(partnerIdx).isPresent()) {
            storeInfo.setStoreIdx(partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA)));
            partnerStoreMapper.update(storeInfo);
        }
        else {
            partnerStoreMapper.save(storeInfo);
            partnerMapper.saveStoreIdx(partnerIdx, storeInfo.getStoreIdx());
        }

        commonStoreRes = new CommonStoreRes(storeInfo.getStoreName(), storeInfo.getStoreInfo(), storeInfo.getStorePhoneNumber(),
                postStoreReq.getStoreBrand(), storeInfo.getStoreLocation(), storeInfo.getStoreImage(), postStoreReq.getStoreImage(),
                storeInfo.getStoreTime(), storeInfo.getBatCount(), storeInfo.getLefthandStatus(), storeInfo.getParkingStatus(),
                storeInfo.getGroupSeatStatus(), storeInfo.getFloorScreenStatus(), storeInfo.getStorageStatus(),
                storeInfo.getLessonStatus(), storeInfo.getReserveStatus(), storeInfo.getCouponStatus());

        return commonStoreRes;
    }

    @Override
    public CommonStoreRes getStore(Long partnerIdx) throws BaseException {
        StoreInfo storeInfo = new StoreInfo();
        List<String> storeImages=null;
        Long storeIdx=null;
        storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA));
        if(storeIdx==null){
            storeInfo.setLefthandStatus(FALSE);
            storeInfo.setParkingStatus(FALSE);
            storeInfo.setGroupSeatStatus(FALSE);
            storeInfo.setFloorScreenStatus(FALSE);
            storeInfo.setStorageStatus(FALSE);
            storeInfo.setLessonStatus(FALSE);
            storeInfo.setReserveStatus(FALSE);
            storeInfo.setCouponStatus(FALSE);
        }
        else{
            storeInfo = partnerStoreMapper.findByStoreIdx(storeIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
            storeImages = storeImageFileMapper.findByStoreIdx(storeIdx);
        }
        if (storeInfo.getStoreImage()==null||storeInfo.getStoreImage().length()==0)
            storeInfo.setStoreInfo("https://i.ibb.co/9hL4Cxk/default-image.jpg");

        String storeBrand="";
        if(storeInfo.getStoreBrand()==null)
            storeBrand = "미 설정";
        else storeBrand=storeInfo.getBrandName();

        CommonStoreRes commonStoreRes = new CommonStoreRes(storeInfo.getStoreName(), storeInfo.getStoreInfo(), storeInfo.getStorePhoneNumber(),
                storeBrand, storeInfo.getStoreLocation(), storeInfo.getStoreImage(),storeImages,
                storeInfo.getStoreTime(), storeInfo.getBatCount(), storeInfo.getLefthandStatus(), storeInfo.getParkingStatus(),
                storeInfo.getGroupSeatStatus(), storeInfo.getFloorScreenStatus(), storeInfo.getStorageStatus(),
                storeInfo.getLessonStatus(), storeInfo.getReserveStatus(), storeInfo.getCouponStatus());
        return commonStoreRes;
    }

    @Override
    public void postCoupon(Long partnerIdx, PostCouponReq postCouponReq) throws BaseException {

        Long storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA));;
        PostCouponInfo postcouponInfo = new PostCouponInfo(storeIdx, postCouponReq.getCouponName(),
                postCouponReq.getCouponPercentage(), postCouponReq.getCouponDeadline());
        partnerStoreMapper.postCouponInfo(postcouponInfo);
        return;
    }
}
