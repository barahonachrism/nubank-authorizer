package com.nubank.authorizer.domain.exceptions;

import com.nubank.authorizer.domain.common.ViolationEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AutorizerException extends RuntimeException{
    private final List<ViolationEnum> violationTypeList;
    public AutorizerException(String message, List<ViolationEnum> violationTypeList){
        super(message);
        this.violationTypeList = violationTypeList;
    }

    public AutorizerException(String message, ViolationEnum violationType){
        super(message);
        this.violationTypeList = new ArrayList<>();
        violationTypeList.add(violationType);
    }
}
