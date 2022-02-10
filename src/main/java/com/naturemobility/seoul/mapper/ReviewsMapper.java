package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.review.ReviewInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewsMapper
{
    int saveScore(ReviewInfo reviewInfo);

    Float getScore(Long reservationIdx);
}
