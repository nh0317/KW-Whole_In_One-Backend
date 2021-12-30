package com.naturemobility.seoul.domain.partners;

import com.naturemobility.seoul.domain.DTOCommon;
import com.naturemobility.seoul.domain.users.UserStatus;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.Part;

@Getter
@Setter
// 사장님 전용
public class PartnerInfo extends DTOCommon {

    // 파트너 인덱스
    private Long partnerIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 파트너 아이디
//    private String partnerId;

    // 파트너 계정 상태 0:활성화, 1:비활성화, 2:탈퇴
    private Integer partnerStatus;

    // 파트너 비밀번호
    private String partnerPassword;

    // 파트너 이메일
    private String partnerEmail;

    // 사장님 이름
    private String partnerName;

    public PartnerInfo(String partnerEmail, String partnerPassword, String partnerName) {
        this.partnerName = partnerName;
        this.partnerPassword = partnerPassword;
        this.partnerEmail = partnerEmail;
    }

    public Boolean getIsAccountNonExpired() {
        return partnerStatus.equals(PartnerStatus.ACTIVE.getValue());
    }

    public Boolean getIsEnabled() {
        return partnerStatus.equals(PartnerStatus.ACTIVE.getValue());
    }
}