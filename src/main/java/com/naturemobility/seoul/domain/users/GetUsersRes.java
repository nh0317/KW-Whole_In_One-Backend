package com.naturemobility.seoul.domain.users;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final Long userId;
    private final String email;
}
