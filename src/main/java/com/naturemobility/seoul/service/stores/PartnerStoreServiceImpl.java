package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.stores.PostStoreReq;
import com.naturemobility.seoul.domain.stores.CommonStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
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

    @Override
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
            storeInfo.setLeftHandsatus(postStoreReq.getLeftHandsatus());
            storeInfo.setParkingStatus(postStoreReq.getParkingStatus());
            storeInfo.setGroupSeatStatus(postStoreReq.getGroupSeatStatus());
            storeInfo.setFloorScreenStatus(postStoreReq.getFloorScreenStatus());
            storeInfo.setStorageStatus(postStoreReq.getStorageStatus());
            storeInfo.setLessonStatus(postStoreReq.getLessonStatus());
            storeInfo.setReserveStatus(postStoreReq.getReserveStatus());
            storeInfo.setCouponStatus(postStoreReq.getCouponStatus());
            //TODO: storeImage 테이블에 저장하는 로직 추가
            //TODO: 새로운 Brand 추가시 try catch문으로 브랜드를 추가하는 로직 추가
            switch (postStoreReq.getStoreBrand()){
                case "미 설정" :
                    storeInfo.setStoreBrand(0L);
                    break;
                case "골프존":
                    storeInfo.setStoreBrand(1L);
                    break;
                case "골프존파크":
                    storeInfo.setStoreBrand(2L);
                    break;
                case "레드골프":
                    storeInfo.setStoreBrand(3L);
                    break;
                case "시티존":
                    storeInfo.setStoreBrand(4L);
                    break;
                case "오케이온":
                    storeInfo.setStoreBrand(5L);
                    break;
                case "프렌즈스크린T":
                    storeInfo.setStoreBrand(6L);
                    break;
                case "프렌즈스크린G":
                    storeInfo.setStoreBrand(7L);
                    break;
                case "SG골프":
                    storeInfo.setStoreBrand(8L);
                    break;
                default:
                    throw new BaseException(RESPONSE_ERROR);
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
            else partnerStoreMapper.save(storeInfo);

            commonStoreRes = new CommonStoreRes(storeInfo.getStoreName(), storeInfo.getStoreInfo(), storeInfo.getStorePhoneNumber(),
                    postStoreReq.getStoreBrand(), storeInfo.getStoreLocation(), storeInfo.getStoreImage(), postStoreReq.getStoreImage(),
                    storeInfo.getStoreTime(), storeInfo.getBatCount(), storeInfo.getLeftHandsatus(), storeInfo.getParkingStatus(),
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
            storeInfo.setLeftHandsatus(FALSE);
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
//                String[] strs =storeInfo.getStoreTime().split("~");
//                String date_time = strs[0].trim();
//                DateTimeFormatter dateParser = DateTimeFormatter.ofPattern("HH:mm");
//                try {
//                    LocalTime open = LocalTime.parse(date_time,dateParser);
//                    LocalTime current = LocalTime.now();
//                    log.info("{}",open);
//                    log.info("{}",current);
//
//                    log.info("{}",open.isBefore(current));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
        switch (storeInfo.getStoreBrand()!=null?storeInfo.getStoreBrand().toString() : "NULL"){
            case "NULL":
            case "0": storeBrand="미 설정";
                break;
            case "1" :
                storeBrand = "골프존";
                break;
            case "2":
                storeBrand = "골프존파크";
                break;
            case "3":
                storeBrand = "레드골프";
                break;
            case "4":
                storeBrand = "시티존";
                break;
            case "5":
                storeBrand = "오케이온";
                break;
            case "6":
                storeBrand = "프렌즈스크린T";
                break;
            case "7":
                storeBrand = "프렌즈스크린G";
            case "8":
                storeBrand = "SG골프";
                break;
            default:
                throw new BaseException(RESPONSE_ERROR);
        }

        CommonStoreRes commonStoreRes = new CommonStoreRes(storeInfo.getStoreName(), storeInfo.getStoreInfo(), storeInfo.getStorePhoneNumber(),
                storeBrand, storeInfo.getStoreLocation(), storeInfo.getStoreImage(),storeImages,
                storeInfo.getStoreTime(), storeInfo.getBatCount(), storeInfo.getLeftHandsatus(), storeInfo.getParkingStatus(),
                storeInfo.getGroupSeatStatus(), storeInfo.getFloorScreenStatus(), storeInfo.getStorageStatus(),
                storeInfo.getLessonStatus(), storeInfo.getReserveStatus(), storeInfo.getCouponStatus());
        return commonStoreRes;
    }
}
