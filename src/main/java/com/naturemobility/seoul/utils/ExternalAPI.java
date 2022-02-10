package com.naturemobility.seoul.utils;

import com.google.gson.Gson;
import com.naturemobility.seoul.config.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@Slf4j
public class ExternalAPI {
    public static Map<String,Object> getResponse(String token, HttpMethod method, Object data, String stringUrl) throws BaseException {
        try{
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            if (token != null)
                httpHeaders.setBearerAuth(token);
            log.info(httpHeaders.toString());

            URI url = UriComponentsBuilder
                    .fromHttpUrl(stringUrl)
                    .build()
                    .encode()
                    .toUri();
            log.info(url.toString());
            ResponseEntity<String> resultJson;
            RestTemplate restTemplate = new RestTemplate();

            if (data == null)
                resultJson = restTemplate.exchange(url, method, new HttpEntity(httpHeaders), String.class);
            else
                resultJson = restTemplate.exchange(url, method, new HttpEntity<>(data, httpHeaders), String.class);

            Gson gson = new Gson();
            log.info(gson.fromJson(resultJson.getBody(), Map.class).toString());
            return gson.fromJson(resultJson.getBody(), Map.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
    }
}
