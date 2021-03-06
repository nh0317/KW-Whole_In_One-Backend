package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginRes {
    private final String userEmail;
    private final String jwt;
    private final String refreshToken;
    private final long jwtValidity;
}
