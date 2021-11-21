package com.naturemobility.seoul.domain.partners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPartnerReq {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
}
