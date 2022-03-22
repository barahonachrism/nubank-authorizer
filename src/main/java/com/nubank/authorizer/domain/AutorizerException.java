package com.nubank.authorizer.domain;

import lombok.Getter;

@Getter
public class AutorizerException extends RuntimeException{
    private final ViolationEnum violationType;
    public AutorizerException(ViolationEnum violationType){
        super(violationType.violationName);
        this.violationType = violationType;
    }
}
