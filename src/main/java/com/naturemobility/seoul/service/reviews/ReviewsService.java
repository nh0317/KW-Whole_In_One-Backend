package com.naturemobility.seoul.service.reviews;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import org.springframework.stereotype.Service;


public interface ReviewsService {
    public PatchReviewsRes saveScore(Long reservationIdx, Long storeIdx, Long userIdx, Float score) throws BaseException;
}
