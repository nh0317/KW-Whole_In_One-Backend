package com.naturemobility.seoul.utils;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.*;
import com.naturemobility.seoul.config.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
//TODO:naver api로 변경하기
public class GeocoderService {
    public Map<String, Double> getGeoDataByAddress(String address) throws BaseException {
        Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("ko").getGeocoderRequest();
        GeocodeResponse geocodeResponse;
        try{
            geocodeResponse = geocoder.geocode(geocoderRequest);
            if(geocodeResponse.getStatus() == GeocoderStatus.OK & !geocodeResponse.getResults().isEmpty()){
                GeocoderResult geocoderResult = geocodeResponse.getResults().iterator().next();
                LatLng latitudeLongitude = geocoderResult.getGeometry().getLocation();

                Map<String, Double> coords = new HashMap<>();
                coords.put("latitude", latitudeLongitude.getLat().doubleValue());
                coords.put("longitude", latitudeLongitude.getLng().doubleValue());
                log.info("latitude : {} longitude : {}", latitudeLongitude.getLat(), latitudeLongitude.getLng());
                return coords;
            }
            else{
                log.info("{} {}", geocodeResponse.getResults(), geocodeResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
