package com.mounanga.customerservice.service;

import com.mounanga.customerservice.dto.CustomerPageResponseDTO;
import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.dto.PageModel;
import com.mounanga.customerservice.exception.CinAlreadyExistException;
import com.mounanga.customerservice.exception.CustomerNotAdultException;
import com.mounanga.customerservice.exception.CustomerNotFoundException;
import com.mounanga.customerservice.exception.EmailAlreadyExistException;

/**
 * Represents a service interface for managing customers.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public interface CustomerService {
    /**
     * Creates a new customer based on the provided CustomerRequestDTO.
     *
     * @param dto the CustomerRequestDTO containing information about the customer to be created.
     * @return a CustomerResponseDTO representing the newly created customer.
     * @throws CinAlreadyExistException if the provided CustomerRequestDTO contains a CIN (Customer Identification Number) that already exists.
     * @throws EmailAlreadyExistException if the provided CustomerRequestDTO contains an email address that already exists.
     * @throws CustomerNotAdultException if the provided CustomerRequestDTO indicates that the customer is not an adult.
     */
    CustomerResponseDTO createCustomer(CustomerRequestDTO dto) throws CinAlreadyExistException, EmailAlreadyExistException, CustomerNotAdultException;

    /**
     * Updates an existing customer with the provided ID, using the information from the CustomerRequestDTO.
     *
     * @param id the ID of the customer to be updated.
     * @param dto the CustomerRequestDTO containing updated information about the customer.
     * @return a CustomerResponseDTO representing the updated customer.
     * @throws CustomerNotFoundException if no customer with the provided ID is found.
     * @throws CinAlreadyExistException if the provided CustomerRequestDTO contains a CIN (Customer Identification Number) that already exists.
     * @throws EmailAlreadyExistException if the provided CustomerRequestDTO contains an email address that already exists.
     * @throws CustomerNotAdultException if the provided CustomerRequestDTO indicates that the customer is not an adult.
     */
    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO dto) throws CustomerNotFoundException, CinAlreadyExistException, EmailAlreadyExistException, CustomerNotAdultException;

    /**
     * Retrieves a customer by ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return The response DTO containing the details of the retrieved customer.
     * @throws CustomerNotFoundException if the customer with the given ID is not found.
     */
    CustomerResponseDTO getCustomerById(String id) throws CustomerNotFoundException;

    /**
     * Retrieves all customers with pagination.
     *
     * @param page The page number.
     * @param size The size of each page.
     * @return The response DTO containing a page of customers.
     */
    PageModel<CustomerResponseDTO> getAllCustomers(int page, int size);

    /**
     * Searches for customers based on a keyword with pagination.
     *
     * @param keyword The keyword to search for.
     * @param page    The page number.
     * @param size    The size of each page.
     * @return The response DTO containing a page of searched customers.
     */
    PageModel<CustomerResponseDTO> searchCustomers(String keyword, int page, int size);

    /**
     * Deletes a customer by ID.
     *
     * @param id The ID of the customer to delete.
     */
    void deleteCustomerById(String id);
}

