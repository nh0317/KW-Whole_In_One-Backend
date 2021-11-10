package Nature_Mobility.Whole_In_One.Backend.domain.advertisement;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
// 광고
public class AdvertisementInfo extends DTOCommon {

    // 광고 인덱스
    private Long adIdx;

    // 광고이름
    private String adName;

    // 광고링크
    private String adLink;

    // 광고이미지
    private String adImage;

    // 게시 상태 여부 0:미게시 , 1:게시
    private Boolean status;
}