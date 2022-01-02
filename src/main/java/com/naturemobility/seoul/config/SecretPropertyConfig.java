package com.naturemobility.seoul.config;

import com.naturemobility.seoul.config.secret.Secret;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RefreshScope
public class SecretPropertyConfig {
    @Value("${jwt.secret.user_info_password_key}")
    private String userInfoPasswordKey;

    @Value("${jwt.secret.token_validity_in_seconds}")
    private String tokenValidityInSeconds;

    @Value("${searchapi.naverid}")
    private String naverId;

    @Value("${searchapi.naversecret}")
    private String naverSecret;
}
