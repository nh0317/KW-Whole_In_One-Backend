package Nature_Mobility.Whole_In_One.Backend.domain.users;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import Nature_Mobility.Whole_In_One.Backend.domain.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DTOUsers extends DTOCommon {
    // 유저인덱스
    private Long userIdx;

    // 유저 닉네임
    private String userNickname;

    // 유저이름
    private String userName;

    // 유저 아이디
    private String userId;

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

    //mypage (join)
    private Integer cntReservation; //예약 수
    private Integer cntStoreLike; // 찜한 매장 수
    private Integer cntCoupon; // 쿠폰 수

    public DTOUsers(String email, String userId, String password, String nickname, String userName) {
        this.userEmail = email;
        this.userId=userId;
        this.userPassword = password;
        this.userNickname = nickname;
        this.userName = userName;
    }

    public DTOUsers(String email, String password, String nickname, String userName) {
        this.userEmail = email;
        this.userPassword = password;
        this.userNickname = nickname;
        this.userName = userName;
    }

    public DTOUsers() {}
}
