package com.brodygaudel.accountservice.query.service;

import com.brodygaudel.accountservice.common.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.common.exception.OperationNotFoundException;
import com.brodygaudel.accountservice.query.dto.AccountDTO;
import com.brodygaudel.accountservice.query.dto.OperationDTO;
import com.brodygaudel.accountservice.query.entity.Account;
import com.brodygaudel.accountservice.query.entity.Operation;
import com.brodygaudel.accountservice.query.model.GetAccountByCustomerIdQuery;
import com.brodygaudel.accountservice.query.model.GetAccountByIdQuery;
import com.brodygaudel.accountservice.query.model.GetOperationByIdQuery;
import com.brodygaudel.accountservice.query.model.GetOperationsByAccountIdQuery;
import com.brodygaudel.accountservice.query.repository.AccountRepository;
import com.brodygaudel.accountservice.query.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;

import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling account-related queries.
 * This service class provides methods to handle various queries related to accounts
 * and operations, such as retrieving an account by ID or customer ID, retrieving
 * operations by account ID, and converting account and operation entities to DTOs.
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Service
@Slf4j
public class AccountQueryHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    /**
     * Constructs a new {@code AccountQueryHandlerService} with the specified repositories.
     *
     * @param accountRepository   the repository for accessing {@link Account} entities
     * @param operationRepository the repository for accessing {@link Operation} entities
     */
    public AccountQueryHandlerService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    /**
     * Handles the GetAccountByIdQuery to retrieve an account by its ID.
     *
     * @param query The GetAccountByIdQuery object.
     * @return The AccountDTO object corresponding to the retrieved account.
     * @throws AccountNotFoundException if the account is not found.
     */
    @QueryHandler
    public AccountDTO handle(@NotNull GetAccountByIdQuery query){
        log.info("GetAccountByIdQuery handled");
        Account account = accountRepository.findById(query.getId())
                .orElseThrow( () -> new AccountNotFoundException("account not found"));
        log.info("account found.");
        return fromAccount(account);
    }

    /**
     * Handles the GetAccountByCustomerIdQuery to retrieve an account by customer ID.
     *
     * @param query The GetAccountByCustomerIdQuery object.
     * @return The AccountDTO object corresponding to the retrieved account.
     * @throws AccountNotFoundException if the account is not found.
     */
    @QueryHandler
    public AccountDTO handle(@NotNull GetAccountByCustomerIdQuery query){
        log.info("GetAccountByCustomerIdQuery handled");
        Account account = accountRepository.findByCustomerId(query.getCustomerId())
                .orElseThrow( () -> new AccountNotFoundException("account not found"));
        log.info("account found");
        return fromAccount(account);
    }

    /**
     * Handles the GetOperationByIdQuery to retrieve an operation by its ID.
     *
     * @param query The GetOperationByIdQuery object.
     * @return The OperationDTO object corresponding to the retrieved operation.
     * @throws OperationNotFoundException if the operation is not found.
     */
    @QueryHandler
    public OperationDTO handle(@NotNull GetOperationByIdQuery query){
        log.info("GetOperationByIdQuery handled");
        Operation operation = operationRepository.findById(query.getId())
                .orElseThrow( () -> new OperationNotFoundException("operation not found"));
        log.info("operation found");
        return fromOperation(operation);
    }

    /**
     * Handles the GetOperationsByAccountIdQuery to retrieve operations by account ID.
     *
     * @param query The GetOperationsByAccountIdQuery object.
     * @return List<OperationDTO> object containing operations information.
     */
    @QueryHandler
    public List<OperationDTO> handle(@NotNull GetOperationsByAccountIdQuery query){
        log.info("GetOperationsByAccountIdQuery handled");
        Page<Operation> operationPage = operationRepository.findByAccountId(
                query.getAccountId(), PageRequest.of(query.getPage(), query.getSize())
        );
        log.info("operation(s) found");
        return operationPage.getContent().stream().map(this::fromOperation).toList();
    }

    /**
     * Converts an {@link Account} object to an {@link AccountDTO} object.
     *
     * @param account the {@link Account} object to convert
     * @return the converted {@link AccountDTO} object
     */
    private AccountDTO fromAccount(@NotNull Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .creation(account.getCreation())
                .currency(account.getCurrency())
                .customerId(account.getCustomerId())
                .lastUpdate(account.getLastUpdate())
                .status(account.getStatus())
                .build();
    }

    /**
     * Converts an {@link Operation} object to an {@link OperationDTO} object.
     *
     * @param operation the {@link Operation} object to convert
     * @return the converted {@link OperationDTO} object
     */
    private OperationDTO fromOperation(@NotNull Operation operation){
        return OperationDTO.builder()
                .id(operation.getId())
                .accountId(operation.getAccount().getId())
                .amount(operation.getAmount())
                .description(operation.getDescription())
                .dateTime(operation.getDateTime())
                .type(operation.getType())
                .build();
    }
}
