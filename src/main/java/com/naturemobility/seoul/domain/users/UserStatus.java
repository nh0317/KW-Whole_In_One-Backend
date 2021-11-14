package com.naturemobility.seoul.domain.users;

public enum UserStatus {
    ACTIVE(0), INACTIVE(1), WITHDRAWN(2);

    private final int value;
    private UserStatus(int value) {
        this.value = value;
    }
    public Integer getValue() {
        return value;
    }
}
