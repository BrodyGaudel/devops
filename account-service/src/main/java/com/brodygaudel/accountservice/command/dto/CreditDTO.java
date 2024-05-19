package com.brodygaudel.accountservice.command.dto;

import java.math.BigDecimal;

public record CreditDTO(String accountId, String description, BigDecimal amount) {
}
