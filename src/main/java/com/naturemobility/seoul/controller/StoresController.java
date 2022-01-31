package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.stores.*;
import com.naturemobility.seoul.mapper.VisitedMapper;
import com.naturemobility.seoul.service.stores.StoreService;
//import com.naturemobility.seoul.utils.JwtService;
import com.naturemobility.seoul.service.visited.VisitedService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/stores")
public class StoresController {
    @Autowired
    private StoreService storeService;
    //private JwtService jwtService;

    @Autowired
    private CheckUserService checkUserService;

    @Autowired
    private VisitedService visitedService;

    /**
     * 매장 검색 API
     * [GET] /stores?storeName=매장이름&userLatitude=37.5533535&userLongitude=127.0235435
     *
     * @return BaseResponse<List<SearchStoreRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<SearchStoreRes>> getStores(@RequestParam String storeName, Double userLatitude, Double userLongitude)   throws BaseException{
        if (storeName == null || userLatitude==null || userLatitude==null){
            return new BaseResponse<>(REQUEST_ERROR);
        }
        List<SearchStoreRes> getStoreList = storeService.retrieveStoreList(storeName,userLatitude,userLongitude);
        return new BaseResponse<>(SUCCESS,getStoreList);
    }

    /**
     * 매장 조회 API
     * [GET] /stores/:storeIdx
     *
     * @return BaseResponse<GetStoreRes>
     */
    @ResponseBody
    @GetMapping("/{storeIdx}")
    public BaseResponse<GetStoreRes> getStoresByStoreIdx(@PathVariable Long storeIdx) throws BaseException {
        if (storeIdx == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        //방문한 매장 표시
        try {
            Long userIdx = checkUserService.getUserIdx();
            visitedService.setVistiedStore(storeIdx, userIdx);
        }catch (BaseException e){
            if (!e.getStatus().equals(NEED_LOGIN))
                throw new BaseException(e.getStatus());
        }

        Integer checkStoreIdx = storeService.checkStoreIdx(storeIdx);
        if (checkStoreIdx == 0) {
            return new BaseResponse<>(REQUEST_ERROR); //존재하지 않는 storeIdx 예외처리
        }

        GetStoreRes getStoreList = storeService.retrieveStoreInfoByStoreIdx(storeIdx);
        return new BaseResponse<>(SUCCESS, getStoreList);
    }

    /**
     * 지도 매장 조회 API
     * [GET] /stores/map?userLatitude=37.5533535&userLongitude=127.0235435&orderRule=1
     *
     * Params : orderRule [1:거리 가까운 순, 2:리뷰 순]
     * @return BaseResponse<List<GetStoreResByMap>>
     */
    @ResponseBody
    @GetMapping("/map")
    public BaseResponse<List<GetStoreResByMap>> getStoresByMap(@RequestParam Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException {
        if (userLatitude == null || userLongitude == null || orderRule == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        //orderRule 1-거리 가까운순, 2-리뷰 별점 순, 추가 사항 있을 시 추가예정
        if(orderRule != 1 && orderRule!=2){
            return new BaseResponse<>(REQUEST_ERROR);
        }

        List<GetStoreResByMap> storeResByMap = storeService.retrieveStoreInfoByMap(userLatitude,userLongitude,orderRule);
        return new BaseResponse<>(SUCCESS, storeResByMap);
    }

    /**
     * 지도 매장 조회 API 필터설정 기능 적용하기
     * [GET] /stores/map/filter?userLatitude=37.5533535&userLongitude=127.0235435&orderRule=1&brand=1&lefthandStatus=0&
     * parkingStatus=0&groupseatStatus=0&floorscreenStatus=0&storageStatus=0&lessonStatus=0&distance=10
     *
     * Params : orderRule [1:거리 가까운 순, 2:리뷰 순]
     *          lefthandStatus  0,1
     *          parkingStatus  0,1
     *          groupseatStatus 0,1
     *          floorscreenStatus 0,1
     *          storageStatus 0,1
     *          lessonStatus 0,1
     *          brand [1:골프존, 2:골프존파크, 3:레드골프, 4:시티존, 5:오케이온, 6:프렌즈스크린, 7:프렌즈스크린G, 8:SG골프 9:프렌즈 스크린T]
     *          distance [1~8 까지의 숫자 (KM 단위)]
     *
     *
     * @return BaseResponse<List<GetStoreResByMap>>
     */
    @ResponseBody
    @GetMapping("/map/filter2")
    public BaseResponse<List<GetStoreResByMap>> getStoresByMapWithFilter(@RequestParam StoreInfoReqByMap storeInfoReqByMap)  throws BaseException {
        if (storeInfoReqByMap.getLefthandStatus() == null || storeInfoReqByMap.getParkingStatus() == null || storeInfoReqByMap.getGroupseatStatus() == null ||
                storeInfoReqByMap.getStorageStatus() == null || storeInfoReqByMap.getLessonStatus() == null || storeInfoReqByMap.getStorageStatus() == null ||
                storeInfoReqByMap.getBrand() == null || storeInfoReqByMap.getDistance() == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        //orderRule 1-거리 가까운순, 2-리뷰 별점 순
        if (storeInfoReqByMap.getOrderRule() != 1 && storeInfoReqByMap.getOrderRule() != 2) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        List<GetStoreResByMap> storeResByMap = storeService.retrieveStoreInfoByMapWithFilter(storeInfoReqByMap);
        return new BaseResponse<>(SUCCESS, storeResByMap);
    }

    @ResponseBody
    @GetMapping("/map/filter")
    public BaseResponse<List<GetStoreResByMap>> getStoresByMapWithFilter2(@RequestParam Double userLatitude, @RequestParam Double userLongitude, @RequestParam Integer orderRule,
                                                                          @RequestParam Integer[] brand, @RequestParam Integer lefthandStatus, @RequestParam Integer parkingStatus,
                                                                          @RequestParam Integer groupseatStatus, @RequestParam Integer floorscreenStatus, @RequestParam Integer storageStatus,
                                                                          @RequestParam Integer lessonStatus, @RequestParam Integer distance) throws BaseException {
        //orderRule 1-거리 가까운순, 2-리뷰 별점 순
        if (orderRule != 1 && orderRule != 2) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        List<GetStoreResByMap> storeResByMap = storeService.retrieveStoreInfoByMapWithFilter2(userLatitude, userLongitude, orderRule, brand, lefthandStatus, parkingStatus, groupseatStatus,
                floorscreenStatus, storageStatus, lessonStatus, distance);
        return new BaseResponse<>(SUCCESS, storeResByMap);
    }

    /**
     * 매장 brand 조회 API
     * [GET] /stores/brand
     *
     * @return BaseResponse<List < GetBrandRes>>
     */
    @ResponseBody
    @GetMapping("/brand")
    public BaseResponse<List<GetBrandRes>> getStoresByMap()  throws BaseException{
        List<GetBrandRes> brandRes = storeService.retrieveBrandInfo();
        return new BaseResponse<>(SUCCESS, brandRes);
    }

    /**
     * 매장 roomIdx 조회 API
     * [GET] /stores/roomIdx?storeIdx=10
     *
     * @return BaseResponse<List < GetRoomIdxRes>>
     */
    @ResponseBody
    @GetMapping("/roomIdx")
    public BaseResponse<List<GetRoomIdxRes>> getRoomIdx(@RequestParam Long storeIdx) throws BaseException {
        List<GetRoomIdxRes> roomIdxRes = storeService.retrieveRoomIdx(storeIdx);
        return new BaseResponse<>(SUCCESS, roomIdxRes);
    }
}




