package com.nubank.authorizer.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nubank.authorizer.application.controllers.TransactionController;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Implement scenarios of functional test scenarios written in Gherkin format
 */
@Slf4j
public class TransactionFunctionalTestStepDef {
    private final String inputFolderName = "/inputs/";
    private final String outputFolderName = "/outputs/";
    private String request;
    private String response;

    @Autowired
    private TransactionController transactionController;

    @Given("File input {string} file")
    public void file_input_file(String requestFileName) throws URISyntaxException, IOException {
        request = readFileToString(inputFolderName, requestFileName);
    }

    @When("Process request")
    public void process_request() throws JsonProcessingException {
        UUID idAccount = UUID.randomUUID();
        response = transactionController.processTransactions(idAccount,request);
    }

    @Then("Output response is equal to {string} file")
    public void output_response_is_equal_to_file(String responseFileName) throws IOException, URISyntaxException {
        String responseExpected = readFileToString(outputFolderName, responseFileName);
        log.info(responseExpected);
        log.info(response);
        log.info("Son iguales:{}", responseExpected.equals(response));
        Assertions.assertEquals(responseExpected, response);

    }

    public String readFileToString(String parentFolder, String fileName) throws IOException, URISyntaxException {
        String path = parentFolder + fileName;
        File file = new File(this.getClass().getResource(path).toURI());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }
        fileReader.close();
        return stringBuffer.toString();
    }
}
