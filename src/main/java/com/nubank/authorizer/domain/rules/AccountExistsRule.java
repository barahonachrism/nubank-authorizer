package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import com.nubank.authorizer.domain.usecase.IAccountRule;
import com.nubank.authorizer.infrastructure.entities.Account;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This rule validate that not exists a previous account
 */
@Component
public class AccountExistsRule implements IAccountRule {
    private final ITransactionRepository transactionRepository;

    public AccountExistsRule(ITransactionRepository transactionRepository){
        this.transactionRepository =  transactionRepository;
    }

    @Override
    public Optional<ViolationEnum> validate(Account account) {
        if(account.getId() != null){
            Optional<Account> existingAccount = transactionRepository.findAccountById(account.getId());
            if(existingAccount.isPresent()){
                return Optional.of(ViolationEnum.ACCOUNT_ALREADY_INITIALIZED);
            }
        }
        return Optional.empty();
    }
}
