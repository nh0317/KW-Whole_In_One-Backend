package Nature_Mobility.Whole_In_One.Backend.domain.userPayment;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 카드 결제 정보 저장
public class UserPaymentInfo extends DTOCommon {

    // 유저 인덱스
    private Long userIdx;

    // 카드사
    private String cardName;

    // 카드정보
    private String cardInfo;
}
