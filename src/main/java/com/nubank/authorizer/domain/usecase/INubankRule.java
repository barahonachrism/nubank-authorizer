package com.nubank.authorizer.domain.usecase;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;

import java.util.Optional;

/**
 * Is the base interface for validate a rule in the use case of account or transacctions
 * @param <E> entity to validate
 */
public interface INubankRule <E> {
    Optional<ViolationEnum> validate(E input);
}
