package com.naturemobility.seoul.domain.stores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRoomIdxRes {
    public String roomName;
    public List<Long> roomIdx;

    GetRoomIdxRes() {
        this.roomIdx = null;
    }
}
