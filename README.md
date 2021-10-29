# KW 산학연계 프로젝트


## 로컬 DB 설정방법

Nature_Mobiliy/Whole_In_One/Backend/config/GlobalPropertyConfig.java 파일에서 하단의 코드 수정

```java
@PropertySources({
        //DB 설정 파일 경로 (개인의 디렉토리 구조에 따라 설정하면 됩니다. classpath = src/main/resources)
        @PropertySource( value = "classpath:/properties/config.yaml", ignoreResourceNotFound = true )
//        @PropertySource( value = "file:${user.home}/env/dev-config.properties", ignoreResourceNotFound = true) // 배포시 배포 환경의 디렉토리 주소
})

```

위에서 설정한 DB설정 파일 경로에서 하단의 코드 작성

```java
# MySQL
spring:
  datasource:
    hikari:
      driver-class-name : com.mysql.cj.jdbc.Driver
      jdbc-url : jdbc:mysql://localhost:3306/WholeInOne?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&useSSL=false
      username : MySQL 호스트 이름
      password : MySQL 비밀번호
```
