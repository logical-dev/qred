package se.qred.loan.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OfferStatus {
    PROCESSED("PROCESSED"),
    PENDING("PENDING"),
    CANCELLED("CANCELLED"),
    SIGNED("SIGNED");

    private final String value;

    OfferStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
