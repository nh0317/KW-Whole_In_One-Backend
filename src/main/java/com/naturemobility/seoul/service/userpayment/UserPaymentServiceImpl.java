package com.naturemobility.seoul.service.userpayment;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.domain.userPayment.*;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.UserPaymentMapper;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.utils.IMPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.utils.ExternalAPI.getResponse;


@Service
@Slf4j
public class UserPaymentServiceImpl implements UserPaymentService {
    @Autowired
    UserPaymentMapper userPaymentMapper;
    @Autowired
    StoresMapper storesMapper;
    @Autowired
    SecretPropertyConfig secretPropertyConfig;
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    IMPService impService;

    @Override
    @Transactional
    public PostUserPaymentRes registerUserPayment(String billingKey, Long userIdx)  throws Exception {
//        String key = secretPropertyConfig.getAes256Key();
//        AES256 aes256 = new AES256(key);

//            String url = "https://api.iamport.kr/subscribe/customers/" + billingKey;
        billingKey = URLEncoder.encode(billingKey, "UTF-8");
        log.info(billingKey);
        String url = "https://api.iamport.kr/subscribe/customers/"+billingKey;
        Map<String, Object> result = getResponse(impService.getIMPToken(),HttpMethod.GET,null, url);
        log.info(result.toString());

        String cardCode = ((Map) result.get("response")).get("card_code").toString();
        Integer cardType = ((Double) ((Map) result.get("response")).get("card_type")).intValue();
        String cardNumber = ((Map) result.get("response")).get("card_number").toString();
        String cardCo= CardCode.cardCoOf(cardCode);
        String formattedCardNumber = String.join("-", cardNumber.split("(?<=\\G.{" + 4 + "})"));

        UserPaymentInfo userPaymentInfo = new UserPaymentInfo(userIdx, billingKey,
                formattedCardNumber, cardType, cardCo);
        userPaymentMapper.saveUserPayment(userPaymentInfo);
        return new PostUserPaymentRes(userPaymentInfo.getUserPaymentIdx(), userPaymentInfo.getCardNumber(),
                userPaymentInfo.getCardType(), userPaymentInfo.getCardCode());
    }

    @Override
    public Map<String,String> createBillingKey(Long userIdx) throws Exception {
//        String key = secretPropertyConfig.getAes256Key();
//        AES256 aes256 = new AES256(key);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String billingKey = now.format(formatter)+userIdx;
//        String billingKey = aes256.encrypt(now.format(formatter)+userIdx);
        Map<String, String> result = new HashMap<>();
        result.put("billingKey", billingKey);
        return result;
    }


    @Override
    public List<GetUserPayments> getUserPayments(Long userIdx) throws BaseException{
        return userPaymentMapper.findAllUserPayments(userIdx);
    }

    @Override
   public Map<String,String> getBillingKey(Long userPaymentIdx) throws  Exception{
//        String key = secretPropertyConfig.getAes256Key();
//        AES256 aes256 = new AES256(key);
        String customerUid = userPaymentMapper.findUserPayment(userPaymentIdx)
                .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
//        customerUid = aes256.decrypt(customerUid);
       Map<String, String> result = new HashMap<>();
       result.put("customerUid", customerUid);
       return result;
    }

    @Override
    public PostMain updateMain(PostMain postMain) throws BaseException{
        userPaymentMapper.updateMain(postMain.getIsMain(), postMain.getUserPaymentIdx());
        return postMain;
    }

    @Override
    public GetUserPayments getMain(Long userIdx) throws BaseException{
        return userPaymentMapper.findMainUserPayment(userIdx);
    }


    @Transactional
    @Override
    public void deleteUserPayment(Long userPaymentIdx) throws Exception {
        String billingKey = impService.decryptBillingKey(userPaymentIdx);
        String url = "https://api.iamport.kr/subscribe/customers/" + billingKey;
        Map<String, Object> response = getResponse(impService.getIMPToken(), HttpMethod.DELETE, null, url);
        int code = ((Double) response.get("code")).intValue();
        if (code == 0) userPaymentMapper.deleteUserPayment(userPaymentIdx);
        else throw new BaseException(RESPONSE_ERROR);
    }
}
