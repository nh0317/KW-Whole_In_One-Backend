package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.brand.BrandInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BrandMapper {
    int save(BrandInfo brandInfo);
    Optional<Long> findBrandIdxByBrandName(@Param("brandName") String brandName);
    int delete(BrandInfo brandInfo);
}
