package com.brodygaudel.accountservice.query.dto;

import com.brodygaudel.accountservice.common.enums.OperationType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a DTO (Data Transfer Object) for an operation.
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
public class OperationDTO {
    private String id;
    private LocalDateTime dateTime;
    private String description;
    private OperationType type;
    private BigDecimal amount;
    private String accountId;
}
