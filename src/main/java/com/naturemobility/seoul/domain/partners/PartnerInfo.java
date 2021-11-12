package com.naturemobility.seoul.domain.partners;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 사장님 전용
public class PartnerInfo extends DTOCommon {

    // 파트너 인덱스
    private Long partnerIdx;

    // 골프장 인덱스
    private Integer storeIdx;

    // 파트너 아이디
    private String partnerId;

    // 파트너 계정 상태 0:활성화, 1:비활성화, 2:탈퇴
    private Boolean partnerStatus;

    // 파트너 비밀번호
    private String partnerPassword;

    // 파트너 이메일
    private String partnerEmail;
}