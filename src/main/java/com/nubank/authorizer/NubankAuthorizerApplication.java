package com.nubank.authorizer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NubankAuthorizerApplication {
    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(NubankAuthorizerApplication.class, args);
        log.info("APPLICATION FINISHED");
    }

    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
    }
}