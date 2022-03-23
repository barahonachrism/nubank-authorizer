package com.nubank.authorizer.application.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.authorizer.application.mapper.ITransactionMapper;
import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;
import com.nubank.authorizer.domain.exceptions.AutorizerException;
import com.nubank.authorizer.domain.services.ITransactionService;
import com.nubank.authorizer.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;
import java.util.UUID;

@Slf4j
@Component
public class TransactionController {
    private static final String ACCOUNT_FIELD = "account";
    private final ObjectMapper objectMapper;
    private ITransactionService transactionService;
    public TransactionController(ITransactionService transactionService, ObjectMapper objectMapper){
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    public String processTransactions(UUID idAccount, String transactions) throws JsonProcessingException {
        StringBuilder responseBuilder = new StringBuilder();
        StringTokenizer transactionTokenizer = new StringTokenizer(transactions,"\n");
        while(transactionTokenizer.hasMoreElements()){
            String transactionToProcess = transactionTokenizer.nextToken();
            //If trying create account
            if(transactionToProcess.indexOf(ACCOUNT_FIELD)>=0){
                AccountRequest accountRequest = objectMapper.readValue(transactionToProcess, AccountRequest.class);
                Account account = ITransactionMapper.INSTANCE.accountVoToAccount(accountRequest.getAccount());
                account.setId(idAccount);

                AccountResponse  accountResponse = new AccountResponse();
                try{
                    Account createdAccount = transactionService.createAccount(account);
                    accountResponse.setAccount(ITransactionMapper.INSTANCE.accountToAccountVo(createdAccount));
                } catch(AutorizerException ex){
                    accountResponse.setAccount(accountRequest.getAccount());
                    accountResponse.getViolations().addAll(ex.getViolationTypeList());
                }
                addEntryResponse(responseBuilder,accountResponse);
            }
            //If trying create a transaction
            else{
                TransactionRequest transactionRequest = objectMapper.readValue(transactionToProcess, TransactionRequest.class);
                Transaction transaction = ITransactionMapper.INSTANCE.transactionVoToTransaction(transactionRequest.getTransaction());
                transaction.setAccount(Account.builder().id(idAccount).build());
                AccountResponse accountResponse = new AccountResponse();
                Transaction createdTransaction = null;
                try{
                    createdTransaction = transactionService.createTransaction(transaction);
                    accountResponse.setAccount(ITransactionMapper.INSTANCE.accountToAccountVo(createdTransaction.getAccount()));
                } catch(AutorizerException ex){
                    accountResponse.setAccount(ITransactionMapper.INSTANCE.accountToAccountVo(transaction.getAccount()));
                    accountResponse.getViolations().addAll(ex.getViolationTypeList());
                }

                addEntryResponse(responseBuilder,accountResponse);
            }
        }
        return responseBuilder.toString();
    }

    private StringBuilder addEntryResponse(StringBuilder responseBuilder, BaseResponse response) throws JsonProcessingException {
        responseBuilder.append(objectMapper.writeValueAsString(response));
        responseBuilder.append("\n");
        return responseBuilder;
    }
}
