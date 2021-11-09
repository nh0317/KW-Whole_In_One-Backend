package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class GetMyPageRes {
    private String userImage;
    private String nickName;
    private Integer cntReservation;
    private Integer cntLikeStore;
    private Integer point;
    private Integer cntCoupon;
}
