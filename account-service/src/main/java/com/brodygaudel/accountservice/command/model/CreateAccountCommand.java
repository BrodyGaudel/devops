package com.brodygaudel.accountservice.command.model;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CreateAccountCommand extends BaseCommand<String> {

    private final AccountStatus status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;
    private final LocalDateTime creation;

    public CreateAccountCommand(String id, AccountStatus status, BigDecimal balance, Currency currency, String customerId, LocalDateTime creation) {
        super(id);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
        this.creation = creation;
    }
}
