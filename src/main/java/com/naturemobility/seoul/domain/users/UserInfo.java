package com.naturemobility.seoul.domain.users;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class UserInfo extends DTOCommon {
    // 유저인덱스
    private Long userIdx;

    // 유저 닉네임
    private String userNickname;

    // 유저이름
    private String userName;

    // 유저 비밀번호 sha512
    private String userPassword;

    // 유저 이메일
    private String userEmail;

    // 계정상태 0:활성화, 1:비활성화, 2:탈퇴
    private Integer userStatus;

    // 유저 포인트
    private Integer userPoint;

    // 유저 이미지 profileImage
    private String userImage;

    private String userPhoneNumber;

    //mypage (join)
    private Integer cntReservation; //예약 수
    private Integer cntStoreLike; // 찜한 매장 수
    private Integer cntCoupon; // 쿠폰 수

    public UserInfo(String email, String password, String nickname, String userName,String userPhoneNumber) {
        this.userEmail = email;
        this.userPassword = password;
        this.userNickname = nickname;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
    }

    public UserInfo() {}

    public Boolean getIsAccountNonExpired() {
        return userStatus.equals(UserStatus.ACTIVE.getValue());
    }

    public Boolean getIsEnabled() {
        return userStatus.equals(UserStatus.ACTIVE.getValue());
    }

    public String getUserImage() {
        return Objects.requireNonNullElse(this.userImage, "https://i.ibb.co/xYJ4ZjX/iconmonstr-golf-2-240-3.png");
    }
}
