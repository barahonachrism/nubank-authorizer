package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.usecase.IAccountTransactionRule;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This rule validate that exists account for execute transactions
 */
@Component
public class AccountNotInitializeRule implements IAccountTransactionRule {
    @Override
    public Optional<ViolationEnum> validate(Transaction transaction) {
        if(transaction.getAccount().getId() == null){
            return Optional.of(ViolationEnum.ACCOUNT_NOT_INITIALIZED);
        }
        return Optional.empty();
    }
}
