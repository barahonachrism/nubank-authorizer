package com.nubank.authorizer.infrastructure.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.authorizer.domain.mapper.ITransactionMapper;
import com.nubank.authorizer.infrastructure.entities.Account;
import com.nubank.authorizer.infrastructure.entities.Transaction;
import com.nubank.authorizer.domain.exceptions.AuthorizerException;
import com.nubank.authorizer.domain.usecase.ITransactionUseCase;
import com.nubank.authorizer.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Controller convert operations request in Java Value Objects and write data
 * to database validatin the business rules.
 */
@Slf4j
@Component
public class TransactionController {
    private static final String ACCOUNT_FIELD = "account";
    private final ObjectMapper objectMapper;
    private final ITransactionUseCase transactionService;
    public TransactionController(ITransactionUseCase transactionService, ObjectMapper objectMapper){
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    /**
     * Process operation transacction in string format and populate
     * in database. Example input:
     * <code>
     * {"account": {"active-card": true, "available-limit": 100}}
     * {"transaction": {"merchant": "Burger King", "amount": 20, "time": "2019-02-13T11:00:00.000Z"}}
     * {"transaction": {"merchant": "Habbib's", "amount": 20, "time": "2019-02-13T11:00:01.000Z"}}
     * {"transaction": {"merchant": "McDonald's", "amount": 20, "time": "2019-02-13T11:01:01.000Z"}}
     * {"transaction": {"merchant": "Subway", "amount": 20, "time": "2019-02-13T11:01:31.000Z"}}
     * {"transaction": {"merchant": "Burger King", "amount": 10, "time": "2019-02-13T12:00:00.000Z"}}
     * </code>
     * @param idAccount account identification
     * @param transactions transactions in string format
     * @return Result of process each operation. Example output:
     * <code>
     * {"account":{"active-card":true,"available-limit":100},"violations":[]}
     * {"account":{"active-card":true,"available-limit":80},"violations":[]}
     * {"account":{"active-card":true,"available-limit":60},"violations":[]}
     * {"account":{"active-card":true,"available-limit":40},"violations":[]}
     * {"account":{"active-card":true,"available-limit":40},"violations":["high-frequency-small-interval"]}
     * {"account":{"active-card":true,"available-limit":30},"violations":[]}
     * </code>
     */
    public String processTransactions(UUID idAccount, String transactions) throws JsonProcessingException {
        StringBuilder responseBuilder = new StringBuilder();
        StringTokenizer transactionTokenizer = new StringTokenizer(transactions,"\n");
        while(transactionTokenizer.hasMoreElements()){
            String transactionToProcess = transactionTokenizer.nextToken();
            AccountResponse accountResponse = new AccountResponse();
            //If trying create account
            if(transactionToProcess.contains(ACCOUNT_FIELD)){
                AccountRequest accountRequest = objectMapper.readValue(transactionToProcess, AccountRequest.class);
                Account account = ITransactionMapper.INSTANCE.accountVoToAccount(accountRequest.getAccount());
                account.setId(idAccount);

                try{
                    accountResponse.setAccount(transactionService.createAccount(account));
                } catch(AuthorizerException ex){
                    accountResponse.setAccount(accountRequest.getAccount());
                    accountResponse.getViolations().addAll(ex.getViolationTypeList());
                }

            }
            //If trying to create a transaction
            else{
                TransactionRequest transactionRequest = objectMapper.readValue(transactionToProcess, TransactionRequest.class);
                Transaction transaction = ITransactionMapper.INSTANCE.transactionVoToTransaction(transactionRequest.getTransaction());
                transaction.setAccount(Account.builder().id(idAccount).build());

                try{
                    accountResponse.setAccount(transactionService.createTransaction(transaction));
                } catch(AuthorizerException ex){
                    accountResponse.setAccount(ITransactionMapper.INSTANCE.accountToAccountVo(transaction.getAccount()));
                    accountResponse.getViolations().addAll(ex.getViolationTypeList());
                }
            }
            responseBuilder.append(objectMapper.writeValueAsString(accountResponse));
            responseBuilder.append("\n");
        }
        return responseBuilder.toString();
    }
}
