package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.domain.users.GetUsersRes;
import com.naturemobility.seoul.service.stores.StoreService;
import com.naturemobility.seoul.utils.JwtService;
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

    @Autowired
    private JwtService jwtService;

    /**
     * 매장 검색 API
     * [GET] /stores?storeName=매장이름&userLatitude=37.5533535&userLongitude=127.0235435
     *
     * @return BaseResponse<List < GetUsersRes>>
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
}




