package com.nubank.authorizer.domain.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response entry to return result of process account
 */
@JsonPropertyOrder({"account", "violations" })
@Getter
@Setter
public class AccountResponse extends BaseResponse{
    private AccountVo account;
}
