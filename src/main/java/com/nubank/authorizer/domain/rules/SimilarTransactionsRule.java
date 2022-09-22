package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import com.nubank.authorizer.domain.usecase.ITransactionRule;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This rule validate that not exists transactions with the same amount in a range of seconds,
 * actually between 120 seconds
 */
@Component
public class SimilarTransactionsRule implements ITransactionRule {
    private final ITransactionRepository transactionRepository;

    public SimilarTransactionsRule(ITransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ViolationEnum> validate(Transaction transaction) {
        LocalDateTime quotaDateTimeStart = transaction.getTime().minusSeconds(TransactionLimits.QUOTA_SECONDS_TRANSACTIONS_LIMIT);
        LocalDateTime quotaDateTimeEnd = transaction.getTime();

        Long countSimilarTransactions = transactionRepository.findCountSimilarTransactionsByRangeDate(transaction.getAccount().getId(), quotaDateTimeStart, quotaDateTimeEnd, transaction.getMerchant(), transaction.getAmount());
        if(countSimilarTransactions > 0 ){
            return Optional.of(ViolationEnum.DOUBLED_TRANSACTION);
        }
        return Optional.empty();
    }
}
