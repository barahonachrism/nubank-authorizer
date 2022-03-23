package com.nubank.authorizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class NubankAuthorizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NubankAuthorizerApplication.class, args);
    }


}