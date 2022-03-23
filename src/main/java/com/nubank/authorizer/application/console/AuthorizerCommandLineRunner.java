package com.nubank.authorizer.application.console;

import com.nubank.authorizer.application.controllers.TransactionController;
import com.nubank.authorizer.domain.common.ViolationEnum;
import com.nubank.authorizer.domain.exceptions.AutorizerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.util.UUID;

@Profile("pro")
@Component
@Slf4j
public class AuthorizerCommandLineRunner implements CommandLineRunner, ApplicationContextAware {
    private TransactionController transactionController;
    private ApplicationContext context;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public AuthorizerCommandLineRunner(TransactionController transactionController){
        this.transactionController = transactionController;
    }
    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        StringBuilder request = new StringBuilder();
        while(input.hasNext()){
            request.append(input.nextLine());
            request.append("\n");
        }
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
