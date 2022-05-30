package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.reservations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {
    List<ReservationInfo> findAllByUserIdx(ReservationInfo reservationInfo);
    Optional<ReservationInfo> findByRezIdx(@Param("reservationIdx") Long reservationIdx);
    int cntTotal(@Param("userIdx") Long userIdx);
    Long findStoreIdxByRezIdx(@Param("reservationIdx") Long storeIdx);
    int delete(ReservationInfo reservationInfo);
    void postReservation(PostRezInfo postRezInfo);
    List<GetRezTime> getReservationTime(@Param("storeIdx") Long storeIdx, @Param("reservationDay") String reservationDay,
                                        @Param("roomIdx") Long roomIdx);
    Integer checkDuplication1(@Param("startTime") String startTime,@Param("endTime") String endTime,
                              @Param("storeIdx") Long storeIdx,@Param("roomIdx") Long roomIdx);
    Integer checkDuplication2(@Param("startTime") String startTime,@Param("endTime") String endTime,
                              @Param("storeIdx") Long storeIdx,@Param("roomIdx") Long roomIdx);

    int updateAlreadyUsed(@Param("alreadyUsed")Boolean alreadyUsed, @Param("reservationIdx")Long reservationIdx);

    List<GetRezTimes> getCanRezTime(@Param("firstTime") LocalDateTime firstTime,
                                    @Param("rezDate") String rezDate, @Param("endTime") LocalDateTime endTime,
                                    @Param("storeIdx")Long storeIdx, @Param("roomIdx") Long roomIdx,
                                    @Param("hall")Integer hall, @Param("playTime") Integer playTime);
}
