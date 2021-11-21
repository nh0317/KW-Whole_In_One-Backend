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

## API 
### User API 
* 전체 회원 조회 API  
* 이메일 중복 확인 API  
* 아이디 중복 확인 API  
* 회원가입 API  
* 로그인API  
* 비밀번호 확인 API  
* 마이페이지 API  
* 회원 정보 조회 API  
* 회원 정보 수정 API  
* 비밀번호 수정 API  
* 회원 탈퇴 API  
  
보다 자세한 설명은 아래의 링크에서 확인가능 합니다.    
[API 문서](https://documenter.getpostman.com/view/18157329/UVC5ESgt)