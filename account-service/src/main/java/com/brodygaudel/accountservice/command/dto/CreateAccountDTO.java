package com.brodygaudel.accountservice.command.dto;

import com.brodygaudel.accountservice.common.enums.Currency;

public record CreateAccountDTO(String customerId, Currency currency) {
}
