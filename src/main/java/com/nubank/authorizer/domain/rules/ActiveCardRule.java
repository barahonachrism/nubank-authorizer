package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.usecase.ITransactionRule;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This rule validate that account is active for execute transactions
 */
@Component
public class ActiveCardRule implements ITransactionRule {
    @Override
    public Optional<ViolationEnum> validate(Transaction transaction) {
        if(Boolean.FALSE.equals(transaction.getAccount().getActiveCard())){
            return Optional.of(ViolationEnum.CARD_NOT_ACTIVE);
        }
        return Optional.empty();
    }
}
