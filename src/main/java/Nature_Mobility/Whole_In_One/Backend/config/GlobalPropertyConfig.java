package Nature_Mobility.Whole_In_One.Backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
//@Profile("dev")
@PropertySources({
        @PropertySource( value = "classpath:/properties/config.yaml", ignoreResourceNotFound = true )//db 설정 파일 경로
//        @PropertySource( value = "file:${user.home}/env/dev-config.properties", ignoreResourceNotFound = true) // 배포시 배포 환경의 디렉토리 주소
})

@Getter
public class GlobalPropertyConfig {

    @Value("${driver-class-name}")
    private String driverClassName;

    @Value("${jdbc-url}")
    private String url;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;
}