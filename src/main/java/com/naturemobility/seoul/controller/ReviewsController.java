package com.naturemobility.seoul.controller;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.domain.review.PatchReviewsReq;
import com.naturemobility.seoul.domain.review.PatchReviewsRes;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import com.naturemobility.seoul.service.reviews.ReviewsService;
import com.naturemobility.seoul.utils.CheckUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("review")
@Slf4j
public class ReviewsController {
    @Autowired
    ReservationsService reservationsService;

    @Autowired
    ReviewsService reviewsService;

    @Autowired
    CheckUserService checkUserService;
    /**
     * 평점 추가
     * [PATCH] /review/{reservationIdx}
     * @return BaseResponse<PatchReviewsRes>
     */
    @PatchMapping("/{reservationIdx}")
    public BaseResponse<PatchReviewsRes> saveScore(@PathVariable("reservationIdx")Long reservationIdx, @RequestBody PatchReviewsReq patchRezReq) throws BaseException{
        Long userIdx = checkUserService.getUserIdx();
        PatchReviewsRes patchRezRes = reviewsService.saveScore(reservationIdx, userIdx, patchRezReq.getScore());
        return new BaseResponse<>(SUCCESS, patchRezRes);
    }

    @GetMapping("/{reservationIdx}")
    public BaseResponse<Map<String, Float>> saveScore(@PathVariable("reservationIdx")Long reservationIdx) throws BaseException{
        Map<String, Float> result = reviewsService.getScore(reservationIdx);
        return new BaseResponse<>(SUCCESS, result);
    }
}
