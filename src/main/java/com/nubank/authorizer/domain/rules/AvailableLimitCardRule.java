package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.usecase.ITransactionRule;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This rule validate that transaction amount not is more than available limit in account
 */
@Component
public class AvailableLimitCardRule implements ITransactionRule {
    @Override
    public Optional<ViolationEnum> validate(Transaction transaction) {
        if(transaction.getAccount().getAvailableLimit() < transaction.getAmount()){
            return Optional.of(ViolationEnum.INSUFFICIENT_LIMIT);
        }
        return Optional.empty();
    }
}
