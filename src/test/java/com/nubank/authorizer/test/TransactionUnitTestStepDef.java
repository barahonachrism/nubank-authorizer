package com.nubank.authorizer.test;

import com.nubank.authorizer.domain.common.ViolationEnum;
import com.nubank.authorizer.domain.entities.Account;
import com.nubank.authorizer.domain.entities.Transaction;
import com.nubank.authorizer.domain.exceptions.AuthorizerException;
import com.nubank.authorizer.domain.services.ITransactionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implement scenarios of unit test scenarios written in Gherkin format
 */
@Slf4j
public class TransactionUnitTestStepDef {
    private List<ViolationEnum> violationTypeList;
    private Transaction transaction;
    private Account account;

    @Autowired
    private ITransactionService transactionService;

    @Given("Active card: {booleanValue}, available limit: {int}, and id account: {string}")
    public void active_card_true_available_limit_and_id_account(boolean activeCard, int availableLimit, String idAccount) {
        account = Account.builder().id(UUID.fromString(idAccount)).activeCard(activeCard).availableLimit(availableLimit).build();
    }

    @Given("Active card: {booleanValue}, available limit: {int}")
    public void active_card_true_available_limit(boolean activeCard, int availableLimit) {
        account = Account.builder().activeCard(activeCard).availableLimit(availableLimit).build();
    }


    @Then("^Account is created successfully$")
    public void account_is_created_successfully() throws Throwable {
        Account savedAccount = transactionService.createAccount(account);

        log.info("Created account with id {}", savedAccount.getId());
        Assertions.assertEquals(savedAccount.getId(),account.getId());
    }

    @When("Trying modify existing account")
    public void trying_modify_existing_account() {
        try{
            Account savedAccount = transactionService.createAccount(account);
            savedAccount.setAvailableLimit(1000);
            Account savedAccountAgain = transactionService.createAccount(account);
        } catch(AuthorizerException ex){
            this.violationTypeList = ex.getViolationTypeList();
        }
        Assertions.assertTrue(true);
    }

    @Then("Return account already initialized {string} as violation")
    public void return_account_already_initialized_as_violation(String violationName) {
        for(ViolationEnum violationEnum :violationTypeList){
            log.info(violationEnum.getViolationName());
        }
        Assertions.assertEquals(1, violationTypeList.size());
        Assertions.assertEquals(violationName, violationTypeList.get(0).getViolationName());
    }

    @Given("Account with id {string}")
    public void account_with_id(String idAccount) {
        account = Account.builder().id(UUID.fromString(idAccount)).build();
    }

    @Then("Return violation on create transaction {string}")
    public void return_violation_on_create_transaction(String violationName) {
        try{
            transactionService.createTransaction(transaction);
            Assertions.assertFalse(true,"La transaccion se creo con errores de validacion");
        } catch(AuthorizerException ex){
            Assertions.assertEquals(1,ex.getViolationTypeList().size());
            Assertions.assertEquals(violationName,ex.getViolationTypeList().get(0).getViolationName());
        }
    }

    @Then("Return multiple violations on create transaction: {string},{string}")
    public void return_multiple_violations_on_create_transaction(String violation1, String violation2) {
        List<ViolationEnum> violationEnumExpectedList = new ArrayList<>();
        violationEnumExpectedList.add(ViolationEnum.getEnum(violation1));
        violationEnumExpectedList.add(ViolationEnum.getEnum(violation2));
        try{
            transactionService.createTransaction(transaction);
            Assertions.assertFalse(true,"La transaccion se creo con errores de validacion");
        } catch(AuthorizerException ex){
            Assertions.assertArrayEquals(violationEnumExpectedList.toArray(),ex.getViolationTypeList().toArray());
        }
    }

    @Then("Create transaction successfully")
    public void create_transaction_successfully() {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        log.info("Id transaction: {}",createdTransaction.getId());
    }

    @Given("Transaction amount: {int}, merchant: {string}, time: {string}")
    public void transaction_amount_merchant_time(Integer amountTransaction, String merchant, String time) {
        LocalDateTime timeTransaction = LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        log.info("Time transaction {}",timeTransaction);
        this.transaction = Transaction.builder().account(account)
                .amount(amountTransaction).merchant(merchant).time(timeTransaction).build();
    }
}