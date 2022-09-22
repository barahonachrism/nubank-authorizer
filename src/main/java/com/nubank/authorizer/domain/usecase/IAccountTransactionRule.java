package com.nubank.authorizer.domain.usecase;

import com.nubank.authorizer.infrastructure.entities.Transaction;

/**
 * This interface is used as filter collection of rules for apply
 * to account of transaction creation
 */
public interface IAccountTransactionRule extends INubankRule<Transaction> {

}
