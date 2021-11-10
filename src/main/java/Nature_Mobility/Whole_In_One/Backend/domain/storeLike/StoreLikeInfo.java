package Nature_Mobility.Whole_In_One.Backend.domain.storeLike;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreLikeInfo extends DTOCommon {
    // 골프장 인덱스
    private Long storeIdx;

    // 유저 인덱스
    private Long userIdx;

    // 찜 상태 0:찜 해제, 1:찜
    private Boolean heartStatus;
}
