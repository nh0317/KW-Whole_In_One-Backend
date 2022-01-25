package com.naturemobility.seoul.service.userpayment;

import com.google.gson.Gson;
import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.domain.userPayment.*;
import com.naturemobility.seoul.mapper.PaymentMapper;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.UserPaymentMapper;
import com.naturemobility.seoul.utils.AES256;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.naturemobility.seoul.config.BaseResponseStatus.NOT_FOUND_DATA;
import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;


@Service
@Slf4j
public class UserPaymentServiceImpl implements UserPaymentService {
    @Autowired
    UserPaymentMapper userPaymentMapper;
    @Autowired
    StoresMapper storesMapper;
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    SecretPropertyConfig secretPropertyConfig;

    @Override
    public PostUserPaymentRes registerUserPayment(String billingKey, Long userIdx)  throws Exception {
        try {
            String key = secretPropertyConfig.getAes256Key();
            AES256 aes256 = new AES256(key);

            String token = getToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(token);

            String url = "https://api.iamport.kr/subscribe/customers/" + billingKey;

            Map<String, Object> result = getResponse(httpHeaders, url);
            log.info(result.toString());
            String cardName = ((Map) result.get("response")).get("card_name").toString();
            Integer cardType = ((Double) ((Map) result.get("response")).get("card_type")).intValue();
            String cardNumber = ((Map) result.get("response")).get("card_number").toString();

            String formatedCardNumber = String.join("-", cardNumber.split("(?<=\\G.{" + 4 + "})"));
            UserPaymentInfo userPaymentInfo = new UserPaymentInfo(userIdx, aes256.encrypt(billingKey),
                    formatedCardNumber, cardType, cardName);
            userPaymentMapper.saveUserPayment(userPaymentInfo);
            return new PostUserPaymentRes(userPaymentInfo.getUserPaymentIdx(), userPaymentInfo.getCardNumber(),
                    userPaymentInfo.getCardType(), userPaymentInfo.getCardCode());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public Map<String,String> payment(PostPayReq postPayReq, Long userIdx) throws Exception {
        String storeName = storesMapper.findStoreName(postPayReq.getStoreIdx()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String name = storeName + "_골프예약_" + LocalDateTime.now();
        PostPayRes getPaymentData = requestPay(postPayReq, name);
        String status = validatePayment(getPaymentData, postPayReq, userIdx, name);
        Map<String, String> result = new HashMap<>();
        switch (status) {
            case "ready":
                result.put("paymentStatus", "가상계좌 발급 성공");
                return result;
            case "paid":
                result.put("paymentStatus", "결제 성공");
                return result;
            default:
                throw new BaseException(RESPONSE_ERROR);
        }
    }

    @Override
    public Map<String,String> createBillingKey() throws Exception {
        String key = secretPropertyConfig.getAes256Key();
        AES256 aes256 = new AES256(key);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String billingKey = aes256.encrypt(now.format(formatter));
        Map<String, String> result = new HashMap<>();
        result.put("billingKey", billingKey);
        return result;
    }

    public String getToken() throws BaseException{
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String url = "https://api.iamport.kr/users/getToken";
        Map<String, Object> data = new HashMap<>();
        data.put("imp_key", secretPropertyConfig.getImpKey());
        data.put("imp_secret", secretPropertyConfig.getImpSecret());
        Map<String, Object> result = getResponse(httpHeaders, data,url);
        return ((Map) result.get("response")).get("access_token").toString();
    }

    @Override
    public List<GetUserPayments> getUserPayments(Long userIdx) throws BaseException{
        return userPaymentMapper.findAllUserPayments(userIdx);
    }

    public PostPayRes requestPay(PostPayReq postPayReq, String name) throws Exception{
            String key = secretPropertyConfig.getAes256Key();
            AES256 aes256 = new AES256(key);
            String customerUid = userPaymentMapper.findUserPayment(postPayReq.getUserPaymentIdx())
                    .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
            customerUid = aes256.decrypt(customerUid);

            String token = getToken();
            log.info(token);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(token);

            String url = "https://api.iamport.kr/subscribe/payments/again";
            Map<String, Object> data = new HashMap<>();
            data.put("customer_uid", customerUid);
            data.put("merchant_uid", "order_" + LocalDateTime.now());
            data.put("amount", postPayReq.getAmount());
            data.put("name", name);

            Map<String, Object> response = getResponse(httpHeaders, data,url);
            return new PostPayRes(((Map)response.get("response")).get("imp_uid").toString(),
                    ((Map)response.get("response")).get("merchant_uid").toString());
    }
    public String validatePayment(PostPayRes getPaymentData, PostPayReq postPayReq, Long userIdx, String name) throws BaseException{
        String token = getToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        String url = "https://api.iamport.kr/payments/"+getPaymentData.getImp_uid();
        Map<String, Object> result = getResponse(httpHeaders, url);
        Double amount = (Double) ((Map)result.get("response")).get("amount");
        String status = ((Map)result.get("response")).get("status").toString();
        if (Objects.equals(postPayReq.getAmount(), amount.intValue())){
            switch (status) {
                case "ready":
                    log.info("가상계좌 발급");
                    return "ready";
                case "paid":
                    log.info("결제 완료");
                    paymentMapper.savePayment(new PaymentInfo(getPaymentData.getMerchant_uid(), userIdx,
                            postPayReq.getStoreIdx(), postPayReq.getAmount(), name));
                    return "paid";
                default: throw new BaseException(RESPONSE_ERROR);
            }
        }
        else throw new BaseException(RESPONSE_ERROR);
    }

    private Map<String,Object> getResponse(HttpHeaders httpHeaders, String url) {
        ResponseEntity<String> resultJson;
        RestTemplate restTemplate = new RestTemplate();
        resultJson = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), String.class);
        Gson gson = new Gson();
        return gson.fromJson(resultJson.getBody(), Map.class);
    }

    private Map<String,Object> getResponse(HttpHeaders httpHeaders, Map<String, Object> data, String url) {
        ResponseEntity<String> resultJson;
        RestTemplate restTemplate = new RestTemplate();
        resultJson = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(data,httpHeaders), String.class);
        Gson gson = new Gson();
        return gson.fromJson(resultJson.getBody(), Map.class);
    }

   @Override
   public Map<String,String> getBillingKey(Long userPaymentIdx) throws  Exception{
        String key = secretPropertyConfig.getAes256Key();
        AES256 aes256 = new AES256(key);
        String customerUid = userPaymentMapper.findUserPayment(userPaymentIdx)
                .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        customerUid = aes256.decrypt(customerUid);
       Map<String, String> result = new HashMap<>();
       result.put("customerUid", customerUid);
       return result;
    }
    @Override
    public PostMain updateMain(PostMain postMain) throws Exception{
        userPaymentMapper.updateMain(postMain.getIsMain(), postMain.getUserPaymentIdx());
        return postMain;
    }
}
