package com.brodygaudel.accountservice.common.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountCreditedEvent extends BaseEvent<String>{
    private final String description;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;

    public AccountCreditedEvent(String id, String description, BigDecimal amount, LocalDateTime dateTime) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
    }
}
