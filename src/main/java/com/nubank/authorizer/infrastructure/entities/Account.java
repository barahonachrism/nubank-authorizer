package com.nubank.authorizer.infrastructure.entities;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Credit card account entity
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private UUID id;
    @NotNull
    private Boolean activeCard;
    @NotNull
    private Integer availableLimit;
}
