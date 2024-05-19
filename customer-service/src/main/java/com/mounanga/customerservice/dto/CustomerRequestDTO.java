package com.mounanga.customerservice.dto;

import com.mounanga.customerservice.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Represents a request DTO for creating or updating a customer.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public record CustomerRequestDTO(
        @NotBlank(message = "cin is mandatory: it can not be blank")
        String cin,

        @NotBlank(message = "firstname is mandatory: it can not be blank")
        String firstname,

        @NotBlank(message = "name is mandatory: it can not be blank")
        String name,

        @NotNull(message = "date of birth is mandatory: it can not be null")
        LocalDate dateOfBirth,

        @NotBlank(message = "place of birth is mandatory: it can not be blank")
        String placeOfBirth,

        @NotBlank(message = "nationality is mandatory: it can not be blank")
        String nationality,

        @NotNull(message = "sex is mandatory: it can not be null")
        Sex sex,

        @NotBlank(message = "email is mandatory: it can not be blank")
        @Email(message = "email is not well formatted")
        String email) {
}
