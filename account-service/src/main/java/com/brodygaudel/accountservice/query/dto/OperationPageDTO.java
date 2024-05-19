package com.brodygaudel.accountservice.query.dto;

import lombok.*;

import java.util.List;

/**
 * Represents a DTO (Data Transfer Object) for a page of operations.
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
public class OperationPageDTO {
    private int page;
    private int size;
    private long totalElements;
    private List<OperationDTO> operations;
}
