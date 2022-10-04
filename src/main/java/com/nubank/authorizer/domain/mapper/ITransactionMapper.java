package com.nubank.authorizer.domain.mapper;

import com.nubank.authorizer.infrastructure.entities.Account;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import com.nubank.authorizer.domain.vo.AccountVo;
import com.nubank.authorizer.domain.vo.TransactionVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Convert java entities to value objets an vice versa
 */
@Mapper
public interface ITransactionMapper {
    ITransactionMapper INSTANCE = Mappers.getMapper(ITransactionMapper.class);
    AccountVo accountToAccountVo(Account accountVo);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    Transaction transactionVoToTransaction(TransactionVo transactionVo);
    @Mapping(target = "id", ignore = true)
    Account accountVoToAccount(AccountVo accountVo);
}
