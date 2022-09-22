package com.nubank.authorizer.domain.usecase;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.infrastructure.entities.Account;

import java.util.Optional;

/**
 * This interface discrimine operations that corresponding to account
 */
public interface IAccountRule extends INubankRule<Account>{
    Optional<ViolationEnum> validate(Account account);
}
