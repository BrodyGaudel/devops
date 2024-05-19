package com.brodygaudel.accountservice.query.entity;

import com.brodygaudel.accountservice.common.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Operation {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, updatable = false)
    @NotBlank
    private String description;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @ManyToOne
    private Account account;
}
