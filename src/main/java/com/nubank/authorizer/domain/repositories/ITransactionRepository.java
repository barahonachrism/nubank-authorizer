package com.nubank.authorizer.domain.repositories;

import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Account or transaction operations over database
 */
public interface ITransactionRepository {
    Account createAccount(Account account);
    Account updateAccount(Account account);
    Optional<Account> findAccountById(UUID idAccount);
    Transaction createTransaction(Transaction transaction);
    Long findCountTransactionByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd);
    Long findCountSimilarTransactionsByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd, String merchant, int amount);
}
