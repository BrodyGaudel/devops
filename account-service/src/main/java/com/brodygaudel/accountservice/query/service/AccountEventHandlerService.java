package com.brodygaudel.accountservice.query.service;

import com.brodygaudel.accountservice.common.exception.AccountNotActivatedException;
import com.brodygaudel.accountservice.common.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.common.exception.CustomerAlreadyHaveAccountException;
import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.OperationType;
import com.brodygaudel.accountservice.common.event.*;
import com.brodygaudel.accountservice.query.entity.Account;
import com.brodygaudel.accountservice.query.entity.Operation;
import com.brodygaudel.accountservice.query.repository.AccountRepository;
import com.brodygaudel.accountservice.query.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service class responsible for handling various account-related events.
 * This class is annotated with {@link Service} to mark it as a Spring-managed service,
 * and {@link Transactional} to ensure that each method runs within a transactional context.
 * It utilizes event handling methods annotated with {@link EventHandler} to respond to different types of account events.
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Service
@Transactional
@Slf4j
public class AccountEventHandlerService {


    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    /**
     * Constructs an {@link  AccountEventHandlerService} with the specified repositories.
     *
     * @param accountRepository    The repository for account-related operations.
     * @param operationRepository  The repository for operations related to account transactions.
     */
    public AccountEventHandlerService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    /**
     * Handles the {@link AccountCreatedEvent} by creating a new account if the customer ID does not exist in the repository.
     *
     * @param event The {@link AccountCreatedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountCreatedEvent event) {
        log.info("AccountCreatedEvent received");
        if (accountRepository.customerIdExists(event.getCustomerId())) {
            throw new CustomerAlreadyHaveAccountException("There is already a customer with this id");
        } else {
            Account account = Account.builder()
                    .currency(event.getCurrency())
                    .id(event.getId())
                    .status(event.getStatus())
                    .customerId(event.getCustomerId())
                    .creation(event.getCreation())
                    .balance(event.getBalance())
                    .build();
            Account accountSaved = accountRepository.save(account);
            log.info("Account successfully created at: " + accountSaved.getCreation());
        }
    }

    /**
     * Handles the {@link AccountActivatedEvent} by activating the corresponding account.
     *
     * @param event The {@link AccountActivatedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountActivatedEvent event) {
        log.info("AccountActivatedEvent received");
        Account account = getAccountById(event.getId());
        account.setStatus(event.getStatus());
        account.setLastUpdate(LocalDateTime.now());
        Account accountActivated = accountRepository.save(account);
        log.info("Account successfully activated at: " + accountActivated.getLastUpdate());
    }

    /**
     * Handles the {@link AccountSuspendedEvent} by suspending the corresponding account.
     *
     * @param event The {@link AccountSuspendedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountSuspendedEvent event) {
        log.info("AccountSuspendedEvent received");
        Account account = getAccountById(event.getId());
        account.setStatus(event.getStatus());
        account.setLastUpdate(LocalDateTime.now());
        Account accountActivated = accountRepository.save(account);
        log.info("Account successfully suspended at: " + accountActivated.getLastUpdate());
    }

    /**
     * Handles the {@link AccountDeletedEvent} by deleting the corresponding account.
     *
     * @param event The {@link AccountDeletedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountDeletedEvent event) {
        log.info("AccountDeletedEvent received");
        accountRepository.deleteById(event.getId());
        log.info("Account successfully deleted.");
    }

    /**
     * Handles the {@link AccountCreditedEvent} by crediting the corresponding account if it is activated.
     *
     * @param event The {@link AccountCreditedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountCreditedEvent event) {
        log.info("AccountCreditedEvent received");
        Account account = getAccountById(event.getId());
        if (account.getStatus().equals(AccountStatus.ACTIVATED)) {
            account.setBalance(account.getBalance().add(event.getAmount()));
            apply(account, event.getDateTime(), event.getAmount(), event.getDescription(), "credited", OperationType.CREDIT);
        } else {
            throw new AccountNotActivatedException("Account not activated");
        }
    }

    /**
     * Handles the {@link AccountDebitedEvent} by debiting the corresponding account if it is activated.
     *
     * @param event The {@link AccountDebitedEvent} to handle.
     */
    @EventHandler
    public void handle(@NotNull AccountDebitedEvent event) {
        log.info("AccountDebitedEvent received");
        Account account = getAccountById(event.getId());
        if (account.getStatus().equals(AccountStatus.ACTIVATED)) {
            account.setBalance(account.getBalance().subtract(event.getAmount()));
            apply(account, event.getDateTime(), event.getAmount(), event.getDescription(), "debited", OperationType.DEBIT);
        } else {
            throw new AccountNotActivatedException("Account not activated");
        }
    }

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id The unique identifier of the account to retrieve.
     * @return The account with the specified identifier.
     * @throws AccountNotFoundException If no account is found with the specified identifier.
     */
    private Account getAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id '" + id + "' not found"));
    }

    /**
     * Applies a credit/debit operation to the given account.
     *
     * @param account     The account to which the credit/debit operation is applied. Must not be {@code null}.
     * @param dateTime    The date and time of the credit/debit operation.
     * @param amount      The amount to be credited/debited.
     * @param description A description of the credit/debit operation.
     * @param message     A message indicating the nature of the account update.
     */
    private void apply(@NotNull Account account, LocalDateTime dateTime, BigDecimal amount, String description, String message, OperationType type) {
        account.setLastUpdate(dateTime);
        Operation operation = Operation.builder().account(account).amount(amount).type(type)
                .description(description).dateTime(dateTime).id(UUID.randomUUID().toString())
                .build();
        Operation operationSaved = operationRepository.save(operation);
        log.info("Operation successfully saved at " + operationSaved.getDateTime());
        Account accountCredited = accountRepository.save(account);
        log.info("Account successfully " + message + " at: " + accountCredited.getLastUpdate());
    }

}
