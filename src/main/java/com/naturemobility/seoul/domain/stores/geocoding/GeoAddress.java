package com.naturemobility.seoul.domain.stores.geocoding;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@JsonDeserialize
public class GeoAddress{
    String roadAddress;
    String jibunAddress;
    String englishAddress;
    List<AddressElements> addressElements;
    String x;
    String y;
    Double distance;
}