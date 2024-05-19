package com.brodygaudel.accountservice.command.service;

import com.brodygaudel.accountservice.command.dto.CreateAccountDTO;
import com.brodygaudel.accountservice.command.dto.CreditDTO;
import com.brodygaudel.accountservice.command.dto.DebitDTO;
import com.brodygaudel.accountservice.command.dto.UpdateStatusDTO;

import java.util.concurrent.CompletableFuture;

/**
 * This interface represents a service for handling account commands.
 * Account commands include actions such as creating accounts, updating account status,
 * crediting an account, and debiting an account.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 3.0
 */
public interface AccountCommandService {

    /**
     * Creates a new account based on the provided DTO (Data Transfer Object).
     *
     * @param dto The DTO containing information needed to create the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     *         which will hold a String representing the ID of the newly created account.
     */
    CompletableFuture<String> create(CreateAccountDTO dto);

    /**
     * Updates the status of an existing account based on the provided DTO.
     *
     * @param dto The DTO containing information needed to update the account status.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     *         which will hold a String representing the result of the update operation.
     */
    CompletableFuture<String> update(UpdateStatusDTO dto);

    /**
     * Credits an existing account with the specified amount based on the provided DTO.
     *
     * @param dto The DTO containing information needed to credit the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     *         which will hold a String representing the result of the credit operation.
     */
    CompletableFuture<String> credit(CreditDTO dto);

    /**
     * Debits an existing account with the specified amount based on the provided DTO.
     *
     * @param dto The DTO containing information needed to debit the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     *         which will hold a String representing the result of the debit operation.
     */
    CompletableFuture<String> debit(DebitDTO dto);

    /**
     * Delete an existing account with the specified id
     *
     * @param id The ID of account t
     * @return A CompletableFuture representing the asynchronous result of the operation,
     *         which will hold a String representing the result of the debit operation.
     */
    CompletableFuture<String> delete(String id);
}

