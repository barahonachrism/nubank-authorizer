package com.nubank.authorizer.application;

import com.nubank.authorizer.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TransactionService implements ITransactionService {

    private ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = false)
    public Account createAccount(Account account) {
        if(account.getId() != null){
            Optional<Account> existingAccount = transactionRepository.findAccountById(account.getId());
            if(existingAccount.isPresent()){
                throw new AutorizerException(ViolationEnum.ACCOUNT_ALREADY_INITIALIZED);
            }
        }
        return transactionRepository.createAccount(account);
    }

    @Override
    public Optional<Account> findAccountById(UUID idAccount) {
        return transactionRepository.findAccountById(idAccount);
    }

    @Transactional(readOnly = false)
    public Transaction createTransaction(Transaction transaction){
        Optional<Account> accountOptional = findAccountById(transaction.getIdAccount());
        if(!accountOptional.isPresent()){
            throw new AutorizerException(ViolationEnum.ACCOUNT_NOT_INITIALIZED);
        }
        Account account =  accountOptional.get();
        if(Boolean.FALSE.equals(account.getActiveCard())){
            throw new AutorizerException(ViolationEnum.CARD_NOT_ACTIVE);
        }

        if(account.getAvailableLimit() < transaction.getAmount()){
            throw new AutorizerException(ViolationEnum.INSUFFICIENT_LIMIT);
        }

        LocalDateTime quotaDateTimeStart = transaction.getTime().minusSeconds(TransactionLimits.QUOTA_SECONDS_TRANSACTIONS_LIMIT);

        LocalDateTime quotaDateTimeEnd = transaction.getTime();



        Long countTransactions = transactionRepository.findCountTransactionByRangeDate(transaction.getIdAccount(), quotaDateTimeStart, quotaDateTimeEnd);
        log.info("quotaDateTimeStart:{},quotaDateTimeEnd:{},countTransactions:{}", quotaDateTimeStart, quotaDateTimeEnd, countTransactions);
        if(countTransactions >= TransactionLimits.QUOTA_TRANSACTIONS_LIMIT ){
            throw new AutorizerException(ViolationEnum.HIGH_FREQUENCY_SMALL_INTERVAL);
        }

        Long countSimilarTransactions = transactionRepository.findCountSimilarTransactionsByRangeDate(transaction.getIdAccount(), quotaDateTimeStart, quotaDateTimeEnd, transaction.getMerchant(), transaction.getAmount());
        if(countSimilarTransactions > 0 ){
            throw new AutorizerException(ViolationEnum.DOUBLE_TRANSACTION);
        }

        account.setAvailableLimit(account.getAvailableLimit() - transaction.getAmount());
        transactionRepository.updateAccount(account);
        return transactionRepository.createTransaction(transaction);
    }
}
