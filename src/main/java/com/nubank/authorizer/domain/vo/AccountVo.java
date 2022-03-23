package com.nubank.authorizer.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Value object to transfer information to client of account credit card
 */
@Getter
@Setter
public class AccountVo {
    @JsonProperty("active-card")
    private Boolean activeCard;
    @JsonProperty("available-limit")
    private Integer availableLimit;
}
