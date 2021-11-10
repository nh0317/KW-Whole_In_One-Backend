package Nature_Mobility.Whole_In_One.Backend.domain.reviewIamgeFile;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 리뷰 이미지 파일
public class ReviewImageFileInfo extends DTOCommon {

    // 이미지 인덱스
    private Long imgFileIdx;

    // 리뷰 인덱스
    private Long reviewIdx;

    // 리뷰 이미지
    private String reviewimage;

}