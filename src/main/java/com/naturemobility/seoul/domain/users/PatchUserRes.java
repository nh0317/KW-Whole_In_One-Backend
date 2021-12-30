package com.naturemobility.seoul.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private String nickname;
    private String name;
    private String userImage;
}
