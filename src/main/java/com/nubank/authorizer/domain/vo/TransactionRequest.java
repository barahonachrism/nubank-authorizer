package com.nubank.authorizer.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Represent a entry of transaction operation request
 */
@Getter
@Setter
public class TransactionRequest {
    private TransactionVo transaction;
}
