package com.nubank.authorizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point to run application
 */
@Slf4j
@SpringBootApplication
public class NubankAuthorizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NubankAuthorizerApplication.class, args);
    }


}