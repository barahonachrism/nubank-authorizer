package com.nubank.authorizer;

import io.cucumber.java.ParameterType;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest(classes = NubankAuthorizerApplication.class)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
    @ParameterType("true|false")
    public boolean booleanValue(String value){
        return Boolean.valueOf(value);
    }
}
