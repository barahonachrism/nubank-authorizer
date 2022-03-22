package com.nubank.authorizer.stepdef;

import com.nubank.authorizer.domain.*;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class AccountStepDef {
    private boolean activeCard;
    private int availableLimit;
    private int amountTransaction;
    private UUID idAccount;
    private ViolationEnum violationType;
    private String merchant;
    private LocalDateTime timeTransaction;
    private Transaction transaction;

    @Autowired
    private ITransactionService transactionService;
//Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298b0"
    @Given("Active card: {booleanValue}, available limit: {int}, and id account: {string}")
    public void active_card_true_available_limit_and_id_account(boolean activeCard, int availableLimit, String idAccount) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        this.idAccount = UUID.fromString(idAccount);
    }


    @Then("^Account is created successfully$")
    public void account_is_created_successfully() throws Throwable {
        Account account = Account.builder().id(idAccount).activeCard(activeCard).availableLimit(availableLimit).build();

        Account savedAccount = transactionService.createAccount(account);

        log.info("Created account with id {}", savedAccount.getId());
        Assertions.assertEquals(savedAccount.getId(),account.getId());
    }

    @Given("id credit card: {string}")
    public void id_credit_card(String idAccount) {
        this.idAccount = UUID.fromString(idAccount);
    }

    @When("Trying modify existing account")
    public void trying_modify_existing_account() {
        Account account = Account.builder().id(idAccount).activeCard(true).availableLimit(200).build();
        try{
            Account savedAccount = transactionService.createAccount(account);
            Account savedAccountAgain = transactionService.createAccount(account);
        } catch(AutorizerException ex){
            this.violationType = ex.getViolationType();
        }
        Assertions.assertTrue(true);
    }

    @Then("Return account already initialized {string} as violation")
    public void return_account_already_initialized_as_violation(String violationName) {
        Assertions.assertEquals(violationType.violationName,violationName);
    }

    @Given("Account with id {string}")
    public void non_existing_account_with_id(String idAccount) {
        this.idAccount = UUID.fromString(idAccount);
    }
    @Then("Return violation on create transaction {string}")
    public void return_violation_on_create_transaction(String violationName) {
        try{
            transactionService.createTransaction(transaction);
            Assertions.assertTrue(false,"La transaccion se creo con errores de validacion");
        } catch(AutorizerException ex){
            Assertions.assertEquals(ex.getViolationType().violationName,violationName);
        }
    }

    @Then("Create transaction successfully")
    public void create_transaction_successfully() {
        transactionService.createTransaction(transaction);
    }

    @Given("Transaction amount: {int}, merchant: {string}, time: {string}")
    public void transaction_amount_merchant_time(Integer amountTransaction, String merchant, String time) {
        LocalDateTime timeTransaction = LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        log.info("Time transaction {}",timeTransaction);
        this.transaction = Transaction.builder().idAccount(idAccount)
                .amount(amountTransaction).merchant(merchant).time(timeTransaction).build();
    }
}