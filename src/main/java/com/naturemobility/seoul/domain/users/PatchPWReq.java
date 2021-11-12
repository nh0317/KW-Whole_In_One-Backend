package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchPWReq {
    String password;
    String newPassword;
    String confirmNewPassword;
    public PatchPWReq(){}
}
