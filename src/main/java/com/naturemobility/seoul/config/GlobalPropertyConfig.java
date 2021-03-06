package com.naturemobility.seoul.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Profile("!default")
@RefreshScope
//@PropertySources({
//        @PropertySource( value = "classpath:/properties/config.yaml", ignoreResourceNotFound = true )//db 설정 파일 경로
////        @PropertySource( value = "file:${user.home}/env/dev-config.properties", ignoreResourceNotFound = true) // 배포시 배포 환경의 디렉토리 주소
//})
@Getter
public class GlobalPropertyConfig {
    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String url;

    @Value("${spring.datasource.hikari.username}")
    private String username;

    @Value("${spring.datasource.hikari.password}")
    private String password;

}