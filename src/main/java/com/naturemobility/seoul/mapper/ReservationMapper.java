package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.reservations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {
    List<ReservationInfo> findAllByUserIdx(ReservationInfo reservationInfo);
    Optional<ReservationInfo> findByRezIdx(@Param("reservationIdx") Long reservationIdx);
    Integer cntTotal(@Param("userIdx") Long userIdx);
    Long findStoreIdxByRezIdx(@Param("reservationIdx") Long storeIdx);
    int delete(ReservationInfo reservationInfo);
    void postReservation(PostRezInfo postRezInfo);
    List<GetRezTime> getReservationTime(@Param("storeIdx") Long storeIdx, @Param("reservationDay") String reservationDay,
                                        @Param("hallNumber") Long hallNumber);
}
