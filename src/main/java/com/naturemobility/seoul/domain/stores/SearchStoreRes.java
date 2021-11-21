package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchStoreRes {
    private final String storeImage;
    private final String storeName;
    private final String storeLocation;
    private final Float distanceFromUser;
}
