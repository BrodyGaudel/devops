package com.mounanga.customerservice.util.implementation;

import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.entity.Customer;
import com.mounanga.customerservice.util.Mappers;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link Mappers} interface providing mapping functionalities.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
@Component
public class MappersImpl implements Mappers {
    /**
     * Maps a {@link CustomerRequestDTO} to a {@link Customer}.
     *
     * @param dto The customer request DTO.
     * @return The corresponding customer entity.
     */
    @Override
    public Customer fromCustomerRequestDTO(@NotNull CustomerRequestDTO dto) {
        return Customer.builder()
                .cin(dto.cin())
                .firstname(dto.firstname())
                .name(dto.name())
                .email(dto.email())
                .sex(dto.sex())
                .dateOfBirth(dto.dateOfBirth())
                .placeOfBirth(dto.placeOfBirth())
                .nationality(dto.nationality())
                .build();
    }

    /**
     * Maps a {@link Customer} entity to a {@link CustomerResponseDTO}.
     *
     * @param customer The customer entity.
     * @return The corresponding customer response DTO.
     */
    @Override
    public CustomerResponseDTO fromCustomer(@NotNull Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getCin(),
                customer.getFirstname(),
                customer.getName(),
                customer.getDateOfBirth(),
                customer.getPlaceOfBirth(),
                customer.getNationality(),
                customer.getSex(),
                customer.getEmail(),
                customer.getCreation(),
                customer.getLastUpdated()
        );
    }

    /**
     * Maps a list of {@link Customer} entities to a list of {@link CustomerResponseDTO} DTOs.
     *
     * @param customers The list of customer entities to map.
     * @return The mapped list of customer response DTOs.
     */
    @Override
    public List<CustomerResponseDTO> fromListOfCustomers(@NotNull List<Customer> customers) {
        return customers.stream().map(this::fromCustomer).toList();
    }

}
