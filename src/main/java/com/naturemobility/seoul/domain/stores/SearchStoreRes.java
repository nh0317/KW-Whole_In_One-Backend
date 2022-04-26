package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchStoreRes {
    private Integer storeIdx;
    private String storeImage="https://i.ibb.co/h1kC5wc/image.png";
    private String storeName;
    private String storeBrand;
    private String storeLocation;
    private Float distanceFromUser;
}
