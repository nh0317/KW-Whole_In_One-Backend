package com.naturemobility.seoul.domain.partners;

public enum PartnerStatus {
    ACTIVE(0), INACTIVE(1), WITHDRAWN(2);

    private final int value;
    private PartnerStatus(int value) {
        this.value = value;
    }
    public Integer getValue() {
        return value;
    }
}
