package com.nubank.authorizer.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository {
    Account createAccount(Account account);
    Account updateAccount(Account account);
    Optional<Account> findAccountById(UUID idAccount);
    Transaction createTransaction(Transaction transaction);
    Long findCountTransactionByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd);
    Long findCountSimilarTransactionsByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd, String merchant, int amount);
}
