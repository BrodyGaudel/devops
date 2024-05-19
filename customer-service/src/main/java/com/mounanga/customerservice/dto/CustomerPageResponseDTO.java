package com.mounanga.customerservice.dto;

import java.util.List;

/**
 * Represents a response DTO for a page of customers.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public record CustomerPageResponseDTO(int totalPage, int page, int size, List<CustomerResponseDTO> customers) {
}
