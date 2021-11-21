package com.naturemobility.seoul.domain.partners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class PostPartnerReq {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
}
