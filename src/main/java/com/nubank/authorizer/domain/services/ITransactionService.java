package com.nubank.authorizer.domain.services;

import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;

import java.util.Optional;
import java.util.UUID;

/**
 * Business logic service to process transaction operation
 */
public interface ITransactionService {
    Account createAccount(Account account);
    Optional<Account> findAccountById(UUID idAccount);
    Transaction createTransaction(Transaction transaction);

}
