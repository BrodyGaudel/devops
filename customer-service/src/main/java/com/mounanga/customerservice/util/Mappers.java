package com.mounanga.customerservice.util;

import com.mounanga.customerservice.dto.CustomerPageResponseDTO;
import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents a set of mappers for converting between different types of DTOs and entities.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public interface Mappers {

    /**
     * Maps a {@link CustomerRequestDTO} to a {@link Customer}.
     *
     * @param dto The customer request DTO.
     * @return The corresponding customer entity.
     */
    Customer fromCustomerRequestDTO(CustomerRequestDTO dto);

    /**
     * Maps a {@link Customer} entity to a {@link CustomerResponseDTO}.
     *
     * @param customer The customer entity.
     * @return The corresponding customer response DTO.
     */
    CustomerResponseDTO fromCustomer(Customer customer);


    /**
     * Maps a list of {@link Customer} entities to a list of {@link CustomerResponseDTO} DTOs.
     *
     * @param customers The list of customer entities to map.
     * @return The mapped list of customer response DTOs.
     */
    List<CustomerResponseDTO> fromListOfCustomers(List<Customer> customers);
}

