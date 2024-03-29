package com.nubank.authorizer.domain.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Control exceptions in authorizer app
 */
@Getter
public class AuthorizerException extends RuntimeException{
    private final List<ViolationEnum> violationTypeList;
    public AuthorizerException(String message, List<ViolationEnum> violationTypeList){
        super(message);
        this.violationTypeList = violationTypeList;
    }
}
