package Nature_Mobility.Whole_In_One.Backend.domain.rounding;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 라운딩내역
public class RoundingInfo extends DTOCommon {

    // 라운딩 내역 인덱스
    private Long roundingIdx;

    // 유저 인덱스
    private Long userIdx;

    // 예약 인덱스
    private Long reservationIdx;

    // 최고 타수
    private Double highestHit;

    // 평균 타수
    private Double avgHit;

}