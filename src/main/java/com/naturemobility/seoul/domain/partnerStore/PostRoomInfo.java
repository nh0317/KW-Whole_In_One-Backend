package com.naturemobility.seoul.domain.partnerStore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRoomInfo {

    private String roomName;
    private String roomType;
    private Long partnerIdx;

    public PostRoomInfo(String roomName, String roomType,Long partnerIdx) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.partnerIdx = partnerIdx;
    }
}