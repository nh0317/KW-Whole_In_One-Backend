package com.naturemobility.seoul.domain.partnerStore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRoomInfo {

    private String roomName;
    private Long partnerIdx;

    public PostRoomInfo(String roomName, Long partnerIdx) {
        this.roomName = roomName;
        this.partnerIdx = partnerIdx;
    }
}