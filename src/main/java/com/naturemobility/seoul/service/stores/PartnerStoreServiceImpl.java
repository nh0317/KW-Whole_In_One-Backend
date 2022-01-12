package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.brand.BrandInfo;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        try{
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
                storeInfo.setStoreBrand(brandMapper.findBrandIdxByBrandName(postStoreReq.getStoreBrand()).orElseThrow(() -> new BaseException(RESPONSE_ERROR)));
            }
            else if (postStoreReq.getStoreBrand()=="미 설정"|| postStoreReq.getStoreBrand()==null || postStoreReq.getStoreBrand()==""){
                storeInfo.setStoreBrand(null);
            }
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
                storeInfo.setStoreIdx(partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(RESPONSE_ERROR)));
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
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }

        return commonStoreRes;
    }

    @Override
    public CommonStoreRes getStore(Long partnerIdx) throws BaseException {
        StoreInfo storeInfo = new StoreInfo();
        List<String> storeImages=null;
        Long storeIdx=null;
        try{
            storeIdx = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(RESPONSE_ERROR));
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
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
            try{
                storeInfo = partnerStoreMapper.findByStoreIdx(storeIdx).orElseThrow(() -> new BaseException(RESPONSE_ERROR));
                storeImages = storeImageFileMapper.findByStoreIdx(storeIdx);
            }catch (Exception e){
                e.printStackTrace();
                throw new BaseException(RESPONSE_ERROR);
            }
        }
        if (storeInfo.getStoreImage()==null||storeInfo.getStoreImage().length()==0)
            storeInfo.setStoreInfo("https://i.ibb.co/9hL4Cxk/default-image.jpg");

        //TODO:brand부분 수정
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
}
