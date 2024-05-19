package com.brodygaudel.accountservice.command.dto;

import java.math.BigDecimal;

public record DebitDTO(String accountId, String description, BigDecimal amount) {
}
