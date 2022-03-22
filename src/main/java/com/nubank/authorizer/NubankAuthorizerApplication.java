package com.nubank.authorizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NubankAuthorizerApplication {

    private static Logger LOG = LoggerFactory
            .getLogger(NubankAuthorizerApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(NubankAuthorizerApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
    }
}