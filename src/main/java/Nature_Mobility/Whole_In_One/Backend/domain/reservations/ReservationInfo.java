package Nature_Mobility.Whole_In_One.Backend.domain.reservations;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
// 예약 테이블
public class ReservationInfo extends DTOCommon {

    // 예약 인덱스
    private Long reservationIdx;

    // 골프장 인덱스
    private Long storeIdx;

    // 유저 인덱스
    private Long userIdx;

    // 예약 시간
    private Date reservationTime;

    // 이용시간
    private Integer useTime;

    // 게임수
    private Integer numberOfGame;

    // 선택 홀
    private Integer selectedHall;

    // 가격
    private Integer price;

    // 요청사항
    private String request;

    // 인원
    private Boolean personCount;
}
