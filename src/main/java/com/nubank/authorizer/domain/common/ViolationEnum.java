package com.nubank.authorizer.domain.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ViolationEnum {
    ACCOUNT_ALREADY_INITIALIZED("account-already-initialized"),
    CARD_NOT_ACTIVE("card-not-active"),
    ACCOUNT_NOT_INITIALIZED("account-not-initialized"),
    INSUFFICIENT_LIMIT("insufficient-limit"),
    HIGH_FREQUENCY_SMALL_INTERVAL("high-frequency-small-interval"),
    DOUBLE_TRANSACTION("double-transaction");
    ViolationEnum(String violationName){
        this.violationName = violationName;
    }
    @JsonValue
    private String violationName;
}
