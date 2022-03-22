package com.nubank.authorizer;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        publish = true,
        plugin = {"pretty", "html:build/cucumber/report"},
        glue = "com.nubank.authorizer")
public class CucumberConfigTest {
}
