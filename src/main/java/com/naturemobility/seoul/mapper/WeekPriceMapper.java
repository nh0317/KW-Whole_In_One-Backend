package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.price.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface WeekPriceMapper {
    int savePriceScheme(PriceSchemeInfo priceSchemeInfo);
    int savePrice(StorePriceInfo storePriceInfo);
    int saveHolidayWeek(HolidayWeekInfo holidayWeekInfo);
    int deletePriceScheme(@Param("priceSchemeIdx")Long priceSchemeIdx);
    int deleteStorePrice(@Param("storePriceIdx")Long storePriceIdx);
    Optional<String> findWeekByStoreIdx(@Param("isHoliday")Boolean isHoliday, @Param("storeIdx") Long storeIdx);
    List<GetPriceRes> findWeekPriceByStoreIdx(@Param("isHoliday") Boolean isHoliday, @Param("storeIdx") Long storeIdx);
    List<GetPriceRes> findPeriodPriceByStoreIdx(@Param("isHoliday") Boolean isHoliday, @Param("storeIdx") Long storeIdx);
    Optional<Long> findPriceSchemeByStoreIdx(@Param("storeIdx")Long storeIdx,@Param("storePriceIdx")Long storePriceIdx);
    Optional<Integer> findCurrentPrice(@Param("storeIdx") Long storeIdx,@Param("hole") Integer hole,
                                       @Param("currentDate") LocalDate currentDate, @Param("currentTime") String currentTime);

    Optional<Integer> findCurrentWeekPrice(@Param("storeIdx") Long storeIdx,@Param("hole") Integer hole,
                                           @Param("currentTime") String currentTime, @Param("isHoliday")Boolean isHoliday);

    List<GetPriceRes> findAllPeriodPriceByStoreIdx(@Param("isHoliday") Boolean isHoliday, @Param("storeIdx") Long storeIdx);
}
