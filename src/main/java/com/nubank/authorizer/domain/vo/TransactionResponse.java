package com.nubank.authorizer.domain.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"transaction", "violations" })
@Getter
@Setter
public class TransactionResponse extends BaseResponse{
    private TransactionVo transaction;
}
