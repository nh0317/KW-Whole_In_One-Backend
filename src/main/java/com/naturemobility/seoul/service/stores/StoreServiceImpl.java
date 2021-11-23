package com.naturemobility.seoul.service.stores;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.UserStatus;
import com.naturemobility.seoul.domain.stores.GetStoreRes;
import com.naturemobility.seoul.domain.stores.GetStoreResByMap;
import com.naturemobility.seoul.domain.stores.SearchStoreRes;
import com.naturemobility.seoul.domain.stores.StoreInfo;
import com.naturemobility.seoul.domain.users.GetUsersRes;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;


@Service
@Slf4j
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoresMapper storesMapper;

    @Autowired
    private JwtService jwtService;


    public List<SearchStoreRes> retrieveStoreList(String storeName, Double userLatitude, Double userLongitude) throws BaseException {
        List<SearchStoreRes> storeInfoList;
        //storeInfoList = null;
        try {

            storeInfoList = storesMapper.findBySearch(storeName, userLatitude, userLongitude);

        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
            //ignored.printStackTrace();
        }
        return storeInfoList;
    }

    @Override
    public GetStoreRes retrieveStoreInfoByStoreIdx(Integer storeIdx) throws BaseException {
        GetStoreRes storeInfo;
        try {

            storeInfo = storesMapper.retrieveStoreInfoByStoreIdx(storeIdx);

        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
            //ignored.printStackTrace();
        }
        return storeInfo;
    }

    @Override
    public Integer checkStoreIdx(Integer storeIdx) throws BaseException {
        Integer storeIdxCheck;
        try {

            storeIdxCheck = storesMapper.checkStoreIdx(storeIdx);

        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
            //ignored.printStackTrace();
        }
        return storeIdxCheck;
    }

    @Override
    public List<GetStoreResByMap> retrieveStoreInfoByMap(Double userLatitude, Double userLongitude, Integer orderRule) throws BaseException {
        List<GetStoreResByMap> storeInfoList;
        try {

            storesMapper.setOrderRule(orderRule);
            storeInfoList = storesMapper.retrieveStoreInfoByMap(userLatitude,userLongitude);

        } catch (Exception ignored) {
            throw new BaseException(RESPONSE_ERROR);
            //ignored.printStackTrace();
        }
        return storeInfoList;
    }
}

