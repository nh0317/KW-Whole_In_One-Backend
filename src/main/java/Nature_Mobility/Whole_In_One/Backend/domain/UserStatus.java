package Nature_Mobility.Whole_In_One.Backend.domain;

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
