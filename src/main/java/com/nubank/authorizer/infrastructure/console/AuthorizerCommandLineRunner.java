package com.nubank.authorizer.infrastructure.console;

import com.nubank.authorizer.infrastructure.controllers.TransactionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.util.UUID;

/**
 * Component to process transactions from file operations in command line.
 * Available only in production environment.
 */
@Profile("pro")
@Component
@Slf4j
public class AuthorizerCommandLineRunner implements CommandLineRunner, ApplicationContextAware {
    private final TransactionController transactionController;
    private ApplicationContext context;

    public AuthorizerCommandLineRunner(TransactionController transactionController){
        this.transactionController = transactionController;
    }
    @Override
    public void run(String... args) throws Exception {
        //Catch each line of file when use redirection operator "<"
        // example: app < operations
        Scanner input = new Scanner(System.in);
        StringBuilder request = new StringBuilder();
        while(input.hasNext()){
            request.append(input.nextLine());
            request.append("\n");
        }
        //Process the operations to print result in console
        if(request.length() > 0){
            UUID idAccount = UUID.randomUUID();
            String response = transactionController.processTransactions(idAccount, request.toString());
            System.out.println(response);
            SpringApplication.exit(context, () -> 0);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
