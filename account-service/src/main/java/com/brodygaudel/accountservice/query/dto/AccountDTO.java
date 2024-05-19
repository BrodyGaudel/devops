package com.brodygaudel.accountservice.query.dto;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a DTO (Data Transfer Object) for an account.
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountDTO {
    private String id;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime creation;
    private LocalDateTime lastUpdate;
}
