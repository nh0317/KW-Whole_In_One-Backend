package com.naturemobility.seoul.domain.users;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PatchPWReq {
    String password;
    String newPassword;
    String confirmNewPassword;
}
