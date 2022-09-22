package com.nubank.authorizer.domain.usecase;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.infrastructure.entities.Transaction;

import java.util.Optional;

/**
 * This interface is used for filter rules that apply in transaction creation
 */
public interface ITransactionRule extends INubankRule<Transaction> {
    Optional<ViolationEnum> validate(Transaction transaction);
}
