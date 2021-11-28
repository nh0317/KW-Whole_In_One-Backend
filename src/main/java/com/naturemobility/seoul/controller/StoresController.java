package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.GetStoreResByMap;
import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
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
    public BaseResponse<List<SearchStoreRes>> getStores(@RequestParam String storeName, Double userLatitude, Double userLongitude) {
        if (storeName == null || userLatitude==null || userLatitude==null){
            return new BaseResponse<>(REQUEST_ERROR);
        }

        try {
            List<SearchStoreRes> getStoreList = storeService.retrieveStoreList(storeName,userLatitude,userLongitude);
            return new BaseResponse<>(SUCCESS,getStoreList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 매장 조회 API
     * [GET] /stores/:storeIdx
     *
     * @return BaseResponse<GetStoreRes>
     */
    @ResponseBody
    @GetMapping("/{storeIdx}")
    public BaseResponse<GetStoreRes> getStoresByStoreIdx(@PathVariable Long storeIdx) {
        if (storeIdx == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        //방문한 매장 표시
        try{
            Long userIdx = checkUserService.getUserIdx();
            visitedService.setVistiedStore(storeIdx,userIdx);
        }catch (BaseException exception){
            if(exception.getStatus()!=NEED_LOGIN)
                return new BaseResponse<>(RESPONSE_ERROR);
        }

        try {
            Integer checkStoreIdx = storeService.checkStoreIdx(storeIdx);
            if (checkStoreIdx == 0) {
                return new BaseResponse<>(REQUEST_ERROR); //존재하지 않는 storeIdx 예외처리
            }

            GetStoreRes getStoreList = storeService.retrieveStoreInfoByStoreIdx(storeIdx);
            return new BaseResponse<>(SUCCESS, getStoreList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
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
    public BaseResponse<List<GetStoreResByMap>> getStoresByMap(@RequestParam Double userLatitude, Double userLongitude, Integer orderRule) {
        if (userLatitude == null || userLongitude == null || orderRule == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        //orderRule 1-거리 가까운순, 2-리뷰 별점 순, 추가 사항 있을 시 추가예정
        if(orderRule != 1 && orderRule!=2){
            return new BaseResponse<>(REQUEST_ERROR);
        }

        try {
            List<GetStoreResByMap> storeResByMap = storeService.retrieveStoreInfoByMap(userLatitude,userLongitude,orderRule);
            return new BaseResponse<>(SUCCESS, storeResByMap);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}




