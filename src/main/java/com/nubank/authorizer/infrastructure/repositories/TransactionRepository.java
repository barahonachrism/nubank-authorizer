package com.nubank.authorizer.infrastructure.repositories;

import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import com.nubank.authorizer.domain.entities.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implements SQL sentences of transaction operations
 */
@Repository
public class TransactionRepository implements ITransactionRepository {
    private EntityManager entityManager;
    public TransactionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Account createAccount(Account account) {
        if(account.getId() == null){
            account.setId(UUID.randomUUID());
        }
        entityManager.persist(account);
        return account;
    }

    @Override
    public Account updateAccount(Account account) {
        return entityManager.merge(account);
    }

    @Override
    public Optional<Account> findAccountById(UUID idAccount){
        return Optional.ofNullable(entityManager.find(Account.class,idAccount));
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }

    @Override
    public Long findCountTransactionByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd) {
        Query query = entityManager.createNamedQuery("Transaction.transactionsByQuotas");
        query.setParameter("idAccount", idAccount);
        query.setParameter("quotaDateTimeStart",quotaDateTimeStart);
        query.setParameter("quotaDateTimeEnd",quotaDateTimeEnd);
        return (Long) query.getSingleResult();
    }

    public Long findCountSimilarTransactionsByRangeDate(UUID idAccount, LocalDateTime quotaDateTimeStart, LocalDateTime quotaDateTimeEnd, String merchant, int amount){
        Query query = entityManager.createNamedQuery("Transaction.similarTransactions");
        query.setParameter("idAccount", idAccount);
        query.setParameter("quotaDateTimeStart",quotaDateTimeStart);
        query.setParameter("quotaDateTimeEnd",quotaDateTimeEnd);
        query.setParameter("merchant",merchant);
        query.setParameter("amount",amount);
        return (Long) query.getSingleResult();
    }

}

