package com.brodygaudel.accountservice.common.event;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AccountSuspendedEvent extends BaseEvent<String> {

    private final AccountStatus status;
    private final LocalDateTime dateTime;

    public AccountSuspendedEvent(String id, AccountStatus status, LocalDateTime dateTime) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
    }
}
