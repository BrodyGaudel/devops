package com.brodygaudel.accountservice.command.model;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SuspendAccountCommand extends BaseCommand<String> {
    private final AccountStatus status;
    private final LocalDateTime dateTime;

    public SuspendAccountCommand(String id, AccountStatus status, LocalDateTime dateTime) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
    }
}
