package com.nubank.authorizer.domain.vo;

import com.nubank.authorizer.domain.common.ViolationEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseResponse {
    private List<ViolationEnum> violations = new ArrayList<>();
}
