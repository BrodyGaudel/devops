package com.brodygaudel.accountservice.command.dto;

import com.brodygaudel.accountservice.common.enums.AccountStatus;

public record UpdateStatusDTO(String accountId, AccountStatus status) {
}
