package com.nubank.authorizer.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Value object of transaction entity to client response
 */
@Getter
@Setter
public class TransactionVo {
    @NotNull
    private String merchant;
    @NotNull
    private Integer amount;
    @NotNull
    private LocalDateTime time;
}
