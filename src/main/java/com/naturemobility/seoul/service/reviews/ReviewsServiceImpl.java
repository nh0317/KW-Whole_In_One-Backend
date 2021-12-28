package com.naturemobility.seoul.service.reviews;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import com.naturemobility.seoul.domain.review.ReviewInfo;
import com.naturemobility.seoul.mapper.ReviewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@Service
@Slf4j
public class ReviewsServiceImpl implements ReviewsService {
    @Autowired
    ReviewsMapper reviewsMapper;

    @Override
    public PatchReviewsRes saveScore(Long reservationIdx, Long storeIdx, Long userIdx, Float reviewScore) throws BaseException {
        PatchReviewsRes patchRezRes;
        try{
            ReviewInfo reviewInfo = new ReviewInfo();
            reviewInfo.setReservationIdx(reservationIdx);
            reviewInfo.setReviewScore(reviewScore);
            reviewInfo.setUserIdx(userIdx);
            reviewInfo.setStoreIdx(storeIdx);
            reviewsMapper.saveScore(reviewInfo);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(RESPONSE_ERROR);
        }
        return new PatchReviewsRes(reservationIdx, storeIdx, reviewScore);
    }
}
