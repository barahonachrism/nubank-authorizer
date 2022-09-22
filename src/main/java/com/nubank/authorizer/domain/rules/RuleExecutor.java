package com.nubank.authorizer.domain.rules;

import com.nubank.authorizer.domain.exceptions.ViolationEnum;
import com.nubank.authorizer.domain.usecase.INubankRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class execute a rule list, and collect the set of violations of each rule
 * @param <E> entity to validate
 */
@Component
public class RuleExecutor<E> {

    public List<ViolationEnum> executeRules(E entity, List< ? extends INubankRule<?>> rules){
        List<ViolationEnum> violationRules = new ArrayList<>();
        for(INubankRule rule : rules){
            Optional<ViolationEnum> violationRule = rule.validate(entity);
            if(violationRule.isPresent()){
                violationRules.add(violationRule.get());
            }
        }
        return violationRules;
    }
}
