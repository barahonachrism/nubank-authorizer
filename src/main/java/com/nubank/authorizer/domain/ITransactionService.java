package com.nubank.authorizer.domain;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionService {
    Account createAccount(Account account);
    Optional<Account> findAccountById(UUID idAccount);
    Transaction createTransaction(Transaction transaction);

}
