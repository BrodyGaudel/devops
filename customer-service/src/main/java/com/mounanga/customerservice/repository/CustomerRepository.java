package com.mounanga.customerservice.repository;

import com.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * This interface represents a repository for managing customer entities.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Check if a customer with the given Cin exists.
     *
     * @param cin The Cin to check for existence.
     * @return True if a customer with the given Cin exists, otherwise false.
     */
    @Query("select case when exists(select 1 from Customer c where c.cin = ?1) then true else false end")
    boolean cinExists(String cin);

    /**
     * Check if a customer with the given Email exists.
     *
     * @param email The Email to check for existence.
     * @return True if a customer with the given Email exists, otherwise false.
     */
    @Query("select case when exists(select 1 from Customer c where c.email = ?1) then true else false end")
    boolean emailExists(String email);

    /**
     * Search for customers based on a keyword, with support for pagination.
     *
     * @param keyword  The keyword to search for, which can match Cin, firstname, or name.
     * @param pageable Pagination information.
     * @return A page of customers matching the search criteria.
     */
    @Query("select c from Customer c where c.cin like :keyword or c.firstname like :keyword or c.name like :keyword")
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);
}

