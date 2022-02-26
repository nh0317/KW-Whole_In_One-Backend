package com.naturemobility.seoul.domain.reservations;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRezResByStoreIdx {
    private String storeName;
    private String storeLocation;
}
