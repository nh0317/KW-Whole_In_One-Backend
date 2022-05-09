package com.naturemobility.seoul.domain.brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandInfo {
    private Long brandIdx;
    private String brandName;

    public BrandInfo(String brandName) {
        this.brandName = brandName;
    }
}
