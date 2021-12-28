package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.reservations.GetRezRes;
import com.naturemobility.seoul.domain.reservations.GetRezResByUserIdx;
import com.naturemobility.seoul.domain.reservations.PostRezInfo;
import com.naturemobility.seoul.domain.reservations.ReservationInfo;
import com.naturemobility.seoul.domain.users.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {
    List<ReservationInfo> findAllByUserIdx(ReservationInfo reservationInfo);
    Optional<ReservationInfo> findByRezIdx(@Param("reservationIdx") Long reservationIdx);
    Integer cntTotal(@Param("userIdx") Long userIdx);
    int delete(ReservationInfo reservationInfo);
    void postReservation(PostRezInfo postRezInfo);
}
