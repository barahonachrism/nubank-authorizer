package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import com.nubank.authorizer.domain.usecase.ITransactionRule;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This rule validate that not exists several transactions in a seconds range.
 * Actually the range is 120 seconds
 */
@Component
public class QuotaTransactionLimitRule implements ITransactionRule {
    private final ITransactionRepository transactionRepository;

    public QuotaTransactionLimitRule(ITransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ViolationEnum> validate(Transaction transaction) {
        LocalDateTime quotaDateTimeStart = transaction.getTime().minusSeconds(TransactionLimits.QUOTA_SECONDS_TRANSACTIONS_LIMIT);

        LocalDateTime quotaDateTimeEnd = transaction.getTime();

        Long countTransactions = transactionRepository.findCountTransactionByRangeDate(transaction.getAccount().getId(), quotaDateTimeStart, quotaDateTimeEnd);
        if(countTransactions >= TransactionLimits.QUOTA_TRANSACTIONS_LIMIT ){
            return Optional.of(ViolationEnum.HIGH_FREQUENCY_SMALL_INTERVAL);
        }
        return Optional.empty();
    }
}
