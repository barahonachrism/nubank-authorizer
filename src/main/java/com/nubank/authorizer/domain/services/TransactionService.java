package com.nubank.authorizer.domain.services;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.mapper.ITransactionMapper;
import com.nubank.authorizer.domain.rules.RuleExecutor;
import com.nubank.authorizer.domain.usecase.*;
import com.nubank.authorizer.domain.vo.AccountVo;
import com.nubank.authorizer.infrastructure.entities.Account;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import com.nubank.authorizer.domain.exceptions.AuthorizerException;
import com.nubank.authorizer.domain.repositories.ITransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Process transactions and account operations of credit card account
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class TransactionService implements ITransactionUseCase {

    private final ITransactionRepository transactionRepository;

    private final List<IAccountRule> accountRules;

    private final List<IAccountTransactionRule> accountTransactionRules;

    private final List<ITransactionRule> transactionRules;

    private final RuleExecutor ruleExecutor;

    public TransactionService(ITransactionRepository transactionRepository, List<IAccountRule> accountRules, List<IAccountTransactionRule> accountTransactionRules, List<ITransactionRule> transactionRules,RuleExecutor ruleExecutor){
        this.transactionRepository = transactionRepository;
        this.accountRules = accountRules;
        this.accountTransactionRules = accountTransactionRules;
        this.transactionRules = transactionRules;
        this.ruleExecutor = ruleExecutor;
    }

    /**
     * Create a credit card account
     *
     * @param account is account to create
     * @return created account
     */
    @Transactional
    public AccountVo createAccount(Account account) {
        List<ViolationEnum> violationRules = ruleExecutor.executeRules(account,accountRules);

        if(violationRules.isEmpty()){

            return ITransactionMapper.INSTANCE.accountToAccountVo(transactionRepository.createAccount(account));
        }

        throw new AuthorizerException("Exists account violation rules", violationRules);

    }

    /**
     * Create transactions operations validating several business rules
     *
     * @param transaction transaction to create
     * @return Created transaction
     */
    @Transactional
    public AccountVo createTransaction(Transaction transaction){
        Optional<Account> accountOptional = transactionRepository.findAccountById(transaction.getAccount().getId());
        Account account =  accountOptional.orElse(new Account());
        transaction.setAccount(account);

        List<ViolationEnum> violationRules = ruleExecutor.executeRules(transaction,accountTransactionRules);

        if(!violationRules.isEmpty()){
            throw new AuthorizerException("Account not is valid for transaction",violationRules);
        }

        violationRules.addAll(ruleExecutor.executeRules(transaction, transactionRules));

        if(violationRules.isEmpty()){
            account.setAvailableLimit(account.getAvailableLimit() - transaction.getAmount());
            transactionRepository.updateAccount(account);
            Transaction createdTransaction = transactionRepository.createTransaction(transaction);
            return ITransactionMapper.INSTANCE.accountToAccountVo(createdTransaction.getAccount());
        }

        throw new AuthorizerException("Multiple violations in transaction creation", violationRules);
    }
}
