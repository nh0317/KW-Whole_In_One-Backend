package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.storeImage.StoreImageFileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreImageFileMapper {
    int save(StoreImageFileInfo storeImageFileInfo);
    int update(StoreImageFileInfo storeImageFileInfo);
    int delete(StoreImageFileInfo storeImageFileInfo);
    String findByImgFileIdx(@Param("ImgFileIdx") Long imgFileIdx);
    List<String> findByStoreIdx(@Param("storeIdx") Long storeIdx);
}
