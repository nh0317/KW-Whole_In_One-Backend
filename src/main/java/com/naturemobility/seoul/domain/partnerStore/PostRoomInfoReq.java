package com.naturemobility.seoul.domain.partnerStore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class PostRoomInfoReq {
    private String roomType;
    private Integer count;
}
