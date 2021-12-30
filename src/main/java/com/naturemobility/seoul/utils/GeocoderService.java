package com.naturemobility.seoul.utils;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.domain.stores.geocoding.GeoAddress;
import com.naturemobility.seoul.domain.stores.geocoding.GetGeoRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class GeocoderService {
    public Map<String, Double> getGeoDataByAddress(String address) throws BaseException {
        RestTemplate restTemplate = new RestTemplate();

        /* 헤더정보세팅 */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-APIGW-API-KEY-ID", "dfiszxzp6x");
        httpHeaders.add("X-NCP-APIGW-API-KEY", "XqDrSro0ezluHijuiMl7aN4k9Dq6e0HKop2apgsp");

        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+address;
        ResponseEntity<GetGeoRes> result;
        Map<String, Double> coords = new HashMap<>();
        try {
             result= restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), GetGeoRes.class);
            coords.put("latitude", Double.valueOf(result.getBody().getAddresses().get(0).getY()));
            coords.put("longitude", Double.valueOf(result.getBody().getAddresses().get(0).getX()));
            log.info("주소 : "+address+" -> 위도 : "+coords.get("latitude") +" 경도 : "+coords.get("longitude"));
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
        return coords;

    }
}
