package com.nubank.authorizer.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment transaction entity
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Transaction.transactionsByQuotas", query = "select count(t) from Transaction t where t.account.id = :idAccount and t.time >= :quotaDateTimeStart and t.time <= :quotaDateTimeEnd")
@NamedQuery(name = "Transaction.similarTransactions", query = "select count(t) from Transaction t where t.account.id = :idAccount and t.time >= :quotaDateTimeStart and t.time <= :quotaDateTimeEnd and t.merchant = :merchant and t.amount = :amount")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;
    @NotNull
    private String merchant;
    @NotNull
    private Integer amount;
    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime time;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;
}