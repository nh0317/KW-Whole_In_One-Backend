package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.userCoupons.GetCouponByUserIdx;
import com.naturemobility.seoul.domain.users.GetUserCoupon;
import com.naturemobility.seoul.domain.users.PostUserCoupon;
import com.naturemobility.seoul.domain.users.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UsersMapper {
    int save(UserInfo userInfo);
    int update(UserInfo userInfo);
    int delete(UserInfo userInfo);
    Optional<UserInfo> findByIdx(@Param("userIdx") Long userIdx);
    List<UserInfo> findByStatus(@Param("status") Integer status);
    List<UserInfo> findByEmail(@Param("email") String email);
    List<UserInfo> findByEmailAndStatus(@Param("email") String email, @Param("status") Integer status);
    List<UserInfo> findByStatusAndNicknameIsContaining(@Param("status") Integer status, @Param("word") String word);

    Optional<Integer> cntCoupon(@Param("userIdx") Long userIdx);
    Optional<Integer> cntStoreLike(@Param("userIdx") Long userIdx);
    Optional<Integer> cntReservation(@Param("userIdx") Long userIdx);

    int updateUserPoint(@Param("userIdx")Long userIdx, @Param("userPoint")int point);

    List<GetCouponByUserIdx> getUserCoupons(@Param("userIdx") Long userIdx);

    void postCoupon(PostUserCoupon postUserCoupon);
    void updateUserCoupon(@Param("userIdx")Long userIdx, @Param("couponIdx")Long couponIdx,
                          @Param("couponStatus") Boolean couponStatus);

    List<GetUserCoupon> getUserCouponsList(@Param("userIdx") Long userIdx);
}
