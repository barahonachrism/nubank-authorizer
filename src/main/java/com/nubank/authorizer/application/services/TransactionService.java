package com.nubank.authorizer.application.services;

import com.nubank.authorizer.domain.common.TransactionLimits;
import com.nubank.authorizer.domain.common.ViolationEnum;
import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;
import com.nubank.authorizer.domain.exceptions.AuthorizerException;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import com.nubank.authorizer.domain.services.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Process transactions and account operations of credit card account
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class TransactionService implements ITransactionService {

    private ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    /**
     * Create a credit card account
     * @param account
     * @return created account
     */
    @Transactional(readOnly = false)
    public Account createAccount(Account account) {
        if(account.getId() != null){
            Optional<Account> existingAccount = transactionRepository.findAccountById(account.getId());
            if(existingAccount.isPresent()){
                throw new AuthorizerException("Account already initialized", ViolationEnum.ACCOUNT_ALREADY_INITIALIZED);
            }
        }
        return transactionRepository.createAccount(account);
    }

    /**
     * Find account by ID
     * @param idAccount ID account
     * @return account
     */
    @Override
    public Optional<Account> findAccountById(UUID idAccount) {
        return transactionRepository.findAccountById(idAccount);
    }

    /**
     * Create transactions operations validating several business rules
     * @param transaction transaction to create
     * @return Created transaction
     */
    @Transactional(readOnly = false)
    public Transaction createTransaction(Transaction transaction){
        Optional<Account> accountOptional = findAccountById(transaction.getAccount().getId());
        List<ViolationEnum> violationEnumList = new ArrayList<>();
        if(!accountOptional.isPresent()){
            transaction.setAccount(new Account());
            throw new AuthorizerException("Account not is initialized",ViolationEnum.ACCOUNT_NOT_INITIALIZED);
        }

        Account account =  accountOptional.get();
        transaction.setAccount(account);
        if(Boolean.FALSE.equals(account.getActiveCard())){
            violationEnumList.add(ViolationEnum.CARD_NOT_ACTIVE);
        }

        if(account.getAvailableLimit() < transaction.getAmount()){
            violationEnumList.add(ViolationEnum.INSUFFICIENT_LIMIT);
        }

        LocalDateTime quotaDateTimeStart = transaction.getTime().minusSeconds(TransactionLimits.QUOTA_SECONDS_TRANSACTIONS_LIMIT);

        LocalDateTime quotaDateTimeEnd = transaction.getTime();

        Long countTransactions = transactionRepository.findCountTransactionByRangeDate(transaction.getAccount().getId(), quotaDateTimeStart, quotaDateTimeEnd);
        log.debug("idAccount: {}, merchant: {}, quotaDateTimeStart:{},quotaDateTimeEnd:{},countTransactions:{}", transaction.getAccount().getId(), transaction.getMerchant(), quotaDateTimeStart, quotaDateTimeEnd, countTransactions);
        if(countTransactions >= TransactionLimits.QUOTA_TRANSACTIONS_LIMIT ){
            violationEnumList.add(ViolationEnum.HIGH_FREQUENCY_SMALL_INTERVAL);
        }

        Long countSimilarTransactions = transactionRepository.findCountSimilarTransactionsByRangeDate(transaction.getAccount().getId(), quotaDateTimeStart, quotaDateTimeEnd, transaction.getMerchant(), transaction.getAmount());
        if(countSimilarTransactions > 0 ){
            violationEnumList.add(ViolationEnum.DOUBLED_TRANSACTION);
        }

        if(violationEnumList.isEmpty()){
            account.setAvailableLimit(account.getAvailableLimit() - transaction.getAmount());
            transactionRepository.updateAccount(account);
            return transactionRepository.createTransaction(transaction);
        }
        throw new AuthorizerException("Multiple violations in transaction creation", violationEnumList);
    }
}
