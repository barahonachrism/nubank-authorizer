package com.nubank.authorizer.application.mapper;

import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;
import com.nubank.authorizer.domain.vo.AccountVo;
import com.nubank.authorizer.domain.vo.TransactionVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Convert java entities to value objets an vice versa
 */
@Mapper
public interface ITransactionMapper {
    ITransactionMapper INSTANCE = Mappers.getMapper(ITransactionMapper.class);
    AccountVo accountToAccountVo(Account accountVo);
    Transaction transactionVoToTransaction(TransactionVo transactionVo);
    Account accountVoToAccount(AccountVo accountVo);
}
