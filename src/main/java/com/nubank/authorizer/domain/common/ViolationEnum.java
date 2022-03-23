package com.nubank.authorizer.domain.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Violation to business rules when process the operations
 * of credit card
 */
@Getter
public enum ViolationEnum {
    ACCOUNT_ALREADY_INITIALIZED("account-already-initialized"),
    CARD_NOT_ACTIVE("card-not-active"),
    ACCOUNT_NOT_INITIALIZED("account-not-initialized"),
    INSUFFICIENT_LIMIT("insufficient-limit"),
    HIGH_FREQUENCY_SMALL_INTERVAL("high-frequency-small-interval"),
    DOUBLED_TRANSACTION("doubled-transaction");
    ViolationEnum(String violationName){
        this.violationName = violationName;
    }
    @JsonValue
    private String violationName;

    public static ViolationEnum getEnum(String value) {
        for (ViolationEnum violationEnum : ViolationEnum.values()) {
            if (violationEnum.violationName.equals(value)) {
                return violationEnum;
            }
        }
        throw new IllegalArgumentException("Invalid ViolationEnum value: " + value);
    }
}
