package com.naturemobility.seoul.service.reviews;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import com.naturemobility.seoul.domain.review.ReviewInfo;
import com.naturemobility.seoul.mapper.ReviewsMapper;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@Service
@Slf4j
public class ReviewsServiceImpl implements ReviewsService {
    @Autowired
    ReviewsMapper reviewsMapper;

    @Autowired
    ReservationsService reservationsService;

    @Override
    public PatchReviewsRes saveScore(Long reservationIdx, Long userIdx, Float reviewScore) throws BaseException {
        Long storeIdx = reservationsService.getStoreIdx(reservationIdx);
        ReviewInfo reviewInfo = new ReviewInfo(storeIdx,userIdx,reviewScore,reservationIdx);
        reviewsMapper.saveScore(reviewInfo);
        return new PatchReviewsRes(reservationIdx, storeIdx, reviewScore);
    }

    @Override
    public Map<String,Float> getScore(Long reservationIdx) {
        reviewsMapper.getScore(reservationIdx);
        Map<String, Float> result = new HashMap<>();
        Float reviewScore = reviewsMapper.getScore(reservationIdx);
        result.put("reviewScore", reviewScore);
        return result;
    }
}
