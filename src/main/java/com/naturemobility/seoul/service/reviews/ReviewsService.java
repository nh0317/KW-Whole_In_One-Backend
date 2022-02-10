package com.naturemobility.seoul.service.reviews;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface ReviewsService {
    PatchReviewsRes saveScore(Long reservationIdx, Long userIdx, Float reviewScore) throws BaseException;
    Map<String,Float> getScore(Long reservationIdx) throws BaseException;
}
