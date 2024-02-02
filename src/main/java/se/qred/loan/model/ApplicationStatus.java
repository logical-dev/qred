package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
    PROCESSED("PROCESSED"),
    PENDING("PENDING"),
    CANCELLED("CANCELLED"),
    SIGNED("SIGNED");

    private final String value;

    ApplicationStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
