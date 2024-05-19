package com.brodygaudel.accountservice.query.entity;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Account {

    @Id
    @Column(unique = true, nullable = false, updatable = false, length=16)
    @NotBlank
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(unique = true, nullable = false, updatable = false)
    @NotBlank
    private String customerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creation;

    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Operation> operations;
}
