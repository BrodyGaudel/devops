package com.brodygaudel.accountservice.command.model;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActiveAccountCommand extends BaseCommand<String>{

    private final AccountStatus status;
    private final LocalDateTime dateTime;

    public ActiveAccountCommand(String id, AccountStatus status, LocalDateTime dateTime) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
    }
}
