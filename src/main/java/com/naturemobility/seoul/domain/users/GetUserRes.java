package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final Long userIdx;
    private final String userId;
    private final String email;
    private final String nickname;
    private final String userName;
    private final String userImage;
}