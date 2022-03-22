package com.nubank.authorizer;

import com.nubank.authorizer.NubankAuthorizerApplication;
import io.cucumber.java.ParameterType;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        publish = true,
        plugin = {"pretty", "html:build/cucumber/report"},
        glue = "com.nubank.authorizer")
public class CucumberConfigTest {
}
