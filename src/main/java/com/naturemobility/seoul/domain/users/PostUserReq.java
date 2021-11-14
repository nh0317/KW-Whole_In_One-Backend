package com.naturemobility.seoul.domain.users;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String name;
}
