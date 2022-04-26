package com.naturemobility.seoul.domain.visited;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GetVisitedByUserIdx {
    private Long storeIdx;
    private String storeName;
    private String storeImage="https://i.ibb.co/h1kC5wc/image.png";
    private String storeType;
    private Float reviewStar;
    private Boolean reserveStatus;
    private Boolean couponStatus;
}
