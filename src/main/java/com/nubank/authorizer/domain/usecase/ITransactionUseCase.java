package com.nubank.authorizer.domain.usecase;

import com.nubank.authorizer.domain.vo.AccountVo;
import com.nubank.authorizer.infrastructure.entities.Account;
import com.nubank.authorizer.infrastructure.entities.Transaction;

import java.util.Optional;
import java.util.UUID;

/**
 * Business logic service to process transaction operation
 */
public interface ITransactionUseCase {
    AccountVo createAccount(Account account);

    AccountVo createTransaction(Transaction transaction);

}
