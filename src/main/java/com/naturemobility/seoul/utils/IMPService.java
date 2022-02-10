package com.naturemobility.seoul.utils;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.mapper.UserPaymentMapper;
import com.naturemobility.seoul.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.NOT_FOUND_DATA;
import static com.naturemobility.seoul.utils.ExternalAPI.getResponse;

@Slf4j
@Service
public class IMPService {

    @Autowired
    UsersMapper usersMapper;
    @Autowired
    UserPaymentMapper userPaymentMapper;
    @Autowired
    SecretPropertyConfig secretPropertyConfig;

    public String decryptBillingKey(Long userPaymentIdx) throws Exception{
        String key = secretPropertyConfig.getAes256Key();
        AES256 aes256 = new AES256(key);
        String customerUid = userPaymentMapper.findUserPayment(userPaymentIdx)
                .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        customerUid = aes256.decrypt(customerUid);
        return customerUid;
    }

    public String getIMPToken() throws BaseException{
        String url = "https://api.iamport.kr/users/getToken";
        Map<String, Object> data = new HashMap<>();
        data.put("imp_key", secretPropertyConfig.getImpKey());
        data.put("imp_secret", secretPropertyConfig.getImpSecret());
        log.info("imp_key: "+secretPropertyConfig.getImpKey()+"imp_secret: "+secretPropertyConfig.getImpSecret());
        Map<String, Object> result = getResponse(null, HttpMethod.POST, data,url);
        return ((Map) result.get("response")).get("access_token").toString();
    }
}
