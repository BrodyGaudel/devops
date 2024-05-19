package com.brodygaudel.accountservice.command.service.implementation;

import com.brodygaudel.accountservice.command.dto.*;
import com.brodygaudel.accountservice.common.exception.CustomerNotFoundException;
import com.brodygaudel.accountservice.command.feign.CustomerRestClient;
import com.brodygaudel.accountservice.command.model.*;
import com.brodygaudel.accountservice.command.service.AccountCommandService;
import com.brodygaudel.accountservice.command.util.IdGenerator;
import com.brodygaudel.accountservice.common.enums.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a service for handling account commands.
 * Account commands include actions such as creating accounts, updating account status,
 * crediting an account, and debiting an account.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 3.0
 */
@Service
@Slf4j
@Transactional
public class AccountCommandServiceImpl implements AccountCommandService {

    private final CommandGateway commandGateway;
    private final IdGenerator idGenerator;
    private final CustomerRestClient customerRestClient;

    public AccountCommandServiceImpl(CommandGateway commandGateway, IdGenerator idGenerator, CustomerRestClient customerRestClient) {
        this.commandGateway = commandGateway;
        this.idGenerator = idGenerator;
        this.customerRestClient = customerRestClient;
    }

    /**
     * Creates a new account based on the provided DTO (Data Transfer Object).
     *
     * @param dto The DTO containing information needed to create the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     * which will hold a String representing the ID of the newly created account.
     */
    @Override
    public CompletableFuture<String> create(@NotNull CreateAccountDTO dto) {
        log.info("# In create()");
        if(customerNotExist(dto.customerId())){
            throw new CustomerNotFoundException("customer not found");
        }
        return commandGateway.send(new CreateAccountCommand(idGenerator.autoGenerate(), AccountStatus.CREATED,
                BigDecimal.ZERO, dto.currency(), dto.customerId(), LocalDateTime.now()
        ));
    }

    /**
     * Updates the status of an existing account based on the provided DTO.
     *
     * @param dto The DTO containing information needed to update the account status.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     * which will hold a String representing the result of the update operation.
     */
    @Override
    public CompletableFuture<String> update(@NotNull UpdateStatusDTO dto) {
        log.info("# update()");
        if(dto.status().equals(AccountStatus.ACTIVATED)){
            return commandGateway.send( new ActiveAccountCommand(dto.accountId(), dto.status(), LocalDateTime.now()));
        }
        return commandGateway.send( new SuspendAccountCommand(dto.accountId(), dto.status(), LocalDateTime.now()));
    }

    /**
     * Credits an existing account with the specified amount based on the provided DTO.
     *
     * @param dto The DTO containing information needed to credit the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     * which will hold a String representing the result of the credit operation.
     */
    @Override
    public CompletableFuture<String> credit(@NotNull CreditDTO dto) {
        log.info("# credit()");
        return commandGateway.send( new CreditAccountCommand(
                dto.accountId(), dto.description(), dto.amount(), LocalDateTime.now()
        ));
    }

    /**
     * Debits an existing account with the specified amount based on the provided DTO.
     *
     * @param dto The DTO containing information needed to debit the account.
     * @return A CompletableFuture representing the asynchronous result of the operation,
     * which will hold a String representing the result of the debit operation.
     */
    @Override
    public CompletableFuture<String> debit(@NotNull DebitDTO dto) {
        log.info("# debit()");
        return commandGateway.send( new DebitAccountCommand(
                dto.accountId(), dto.description(), dto.amount(), LocalDateTime.now()
        ));
    }

    /**
     * Delete an existing account with the specified id
     *
     * @param id The ID of account t
     * @return A CompletableFuture representing the asynchronous result of the operation,
     * which will hold a String representing the result of the debit operation.
     */
    @Override
    public CompletableFuture<String> delete(String id) {
        log.info("# delete()");
        return commandGateway.send( new DeleteAccountCommand(id));
    }

    /**
     * Checks if a customer with the specified ID exists.
     *
     * @param customerId The ID of the customer to check for existence.
     * @return {@code true} if a customer with the specified ID exists, {@code false} otherwise.
     */
    private boolean customerNotExist(String customerId) {
        try {
            // Attempt to retrieve the customer information by ID
            CustomerDTO dto = customerRestClient.getById(customerId);
            // Return true if the DTO is not null, indicating the customer exists
            return dto == null;
        } catch (Exception e) {
            log.warn(e.getMessage());
            // In case of any exception during retrieval, return false
            return false;
        }
    }

}
