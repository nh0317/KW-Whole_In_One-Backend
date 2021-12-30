package com.naturemobility.seoul.domain.stores.geocoding;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetGeoRes {
    String status;
    Meta meta;
    List<GeoAddress> addresses;
    String errorMessage;
}
