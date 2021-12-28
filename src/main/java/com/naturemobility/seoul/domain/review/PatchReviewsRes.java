package com.naturemobility.seoul.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchReviewsRes {
    Long reservationIdx;
    Long storeIdx;
    Float score;
}
