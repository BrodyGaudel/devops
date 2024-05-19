package com.brodygaudel.accountservice.command.feign;

import com.brodygaudel.accountservice.command.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Feign client interface for communicating with the "CUSTOMER-SERVICE" service.
 *
 * <p>This interface defines methods for retrieving customer information via HTTP requests.</p>
 *
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@FeignClient(name = "customer-service")
public interface CustomerRestClient {

    /**
     * Retrieves customer details by their unique ID.
     *
     * @param id The unique identifier of the customer.
     * @return The {@code CustomerDTO} containing customer details.
     */
    @GetMapping("/bank/customers/get/{id}")
    CustomerDTO getById(@PathVariable String id);
}
