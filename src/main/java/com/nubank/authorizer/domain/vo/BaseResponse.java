package com.nubank.authorizer.domain.vo;

import com.nubank.authorizer.domain.common.ViolationEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * General response must has violations list, empty as default
 */
@Getter
@Setter
public class BaseResponse {
    private List<ViolationEnum> violations = new ArrayList<>();
}
