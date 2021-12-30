package com.naturemobility.seoul.domain.partners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPartnerRes {
    private Long idx;
    private String name;
    private String email;
}
