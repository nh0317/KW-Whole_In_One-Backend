package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostRefreshTokenRes {
    private final String jwt;
    private final String refreshToken;
    private final long jwtValidity;
}
