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
* 회원가입 API  
* 로그인API  
* 비밀번호 확인 API  
* 마이페이지 API  
* 회원 정보 조회 API  
* 회원 정보 수정 API  
* 비밀번호 수정 API  
* 회원 탈퇴 API  
  
보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### Partener API 
* 이메일 중복 확인 API  
* 회원가입 API  
* 로그인API  
* 비밀번호 확인 API  
* 회원 정보 조회 API  
* 회원 정보 수정 API  
* 비밀번호 수정 API  
* 회원 탈퇴 API  

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### 로그인 주의 사항
* 로그인은 authorization bearer header에 jwt 토큰을 넣는 방식으로 동작합니다. 
* 현재 상태에서 로그인이 필요한 페이지의 경우 DB상의 1번째 유저의 정보를 반환합니다. 
  (현재 상태에서 로그인이 필요한 페이지에서 전부 로그인을 요청할 경우 귀찮을 수 있다고 판단했습니다.)

### Vistied API 
* 방문한 매장(찜한 매장) 목록 조회 API

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### Reservation API 
* 예약 내역 목록 조회 API
* 예약 상세 내용 조회 API

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### Review API
* 매장 평점 추가 API
보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>

[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### PartnerStore API
* 사장님 매장 조회 API
* 매장 등록 또는 수정 API
* 
보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### Price API
* 주말/평일 등록 API
* 주말/평일 조회 API 
* 가격 등록 및 수정 API 
* 주말/평일 가격 조회 API 
* 특정 기간 가격 조회 API 
* 특정 날짜 및 시간의 가격 조회 API 
* 가격 삭제 API 

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)

### userPayment API
* 결제수단 등록 API 
* 결제 요청 API 
* 등록된 카드 조회 API 
* 기본 결제수단 변경 API 

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/18157329/UVR8o7nG)
### Stores API
* 골프장 검색
* 골프장 조회 (storeIdx로 조회)
* 골프장 목록 조회

### 지도 정보 제공 API
* 지도 정보 제공
* 가게 브랜드 정보 제공 API

### Reservation API
* 예약하기 API
* 예약되어 있는 시간 조회 API

보다 자세한 설명은 아래의 링크에서 확인가능 합니다.<br>
[API 문서](https://documenter.getpostman.com/view/14269013/UVJZoJC2)


  
