package com.brodygaudel.accountservice.query.service;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;
import com.brodygaudel.accountservice.common.enums.OperationType;
import com.brodygaudel.accountservice.common.event.*;
import com.brodygaudel.accountservice.common.exception.AccountNotActivatedException;
import com.brodygaudel.accountservice.common.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.common.exception.CustomerAlreadyHaveAccountException;
import com.brodygaudel.accountservice.query.entity.Account;
import com.brodygaudel.accountservice.query.entity.Operation;
import com.brodygaudel.accountservice.query.repository.AccountRepository;
import com.brodygaudel.accountservice.query.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountEventHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private AccountEventHandlerService eventHandlerService;

    @BeforeEach
    void setUp() {
        eventHandlerService = new AccountEventHandlerService(accountRepository, operationRepository);
    }

    @Test
    void testHandleAccountCreatedEvent() {
        AccountCreatedEvent event = new AccountCreatedEvent("1234567887654321", AccountStatus.CREATED,
                BigDecimal.ZERO, Currency.TND, "customerId", LocalDateTime.now()
        );
        Account account = Account.builder().currency(event.getCurrency()).id(event.getId()).status(event.getStatus())
                .customerId(event.getCustomerId()).creation(event.getCreation()).balance(event.getBalance())
                .build();

        when(accountRepository.customerIdExists(anyString())).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(account);

        eventHandlerService.handle(event);
        verify(accountRepository).customerIdExists(anyString());
        verify(accountRepository).save(any());
    }

    @Test
    void testHandleAccountCreatedEventThrowCustomerAlreadyHaveAccountException() {
        AccountCreatedEvent event = new AccountCreatedEvent("1234567887654321", AccountStatus.CREATED,
                BigDecimal.ZERO, Currency.TND, "customerId", LocalDateTime.now()
        );
        when(accountRepository.customerIdExists(anyString())).thenReturn(true);
        assertThrows(CustomerAlreadyHaveAccountException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testHandleAccountActivatedEvent() {
        AccountActivatedEvent event = new AccountActivatedEvent("1234567887654321", AccountStatus.ACTIVATED, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.SUSPENDED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.ZERO)
                .build();
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn( Account.builder().currency(Currency.CAD).id(event.getId())
                .status(AccountStatus.ACTIVATED).customerId("CustomerId").creation(LocalDateTime.now())
                .balance(BigDecimal.ZERO).lastUpdate(LocalDateTime.now())
                .build()
        );

        eventHandlerService.handle(event);
        verify(accountRepository).findById(anyString());
        verify(accountRepository).save(any());
    }

    @Test
    void testHandleHandleAccountActivatedEventThrowAccountNotFoundException() {
        AccountActivatedEvent event = new AccountActivatedEvent("1234567887654321", AccountStatus.ACTIVATED, LocalDateTime.now());
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testHandleAccountSuspendedEvent() {
        AccountSuspendedEvent event = new AccountSuspendedEvent("1234567887654321", AccountStatus.SUSPENDED, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.ACTIVATED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.ZERO)
                .build();
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn( Account.builder().currency(Currency.CAD).id(event.getId())
                .status(AccountStatus.SUSPENDED).customerId("CustomerId").creation(LocalDateTime.now())
                .balance(BigDecimal.ZERO).lastUpdate(LocalDateTime.now())
                .build()
        );

        eventHandlerService.handle(event);
        verify(accountRepository).findById(anyString());
        verify(accountRepository).save(any());
    }

    @Test
    void testHandleHandleAccountSuspendedEventThrowAccountNotFoundException() {
        AccountSuspendedEvent event = new AccountSuspendedEvent("1234567887654321", AccountStatus.SUSPENDED, LocalDateTime.now());
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testHandleAccountDeletedEvent() {
        AccountDeletedEvent event = new AccountDeletedEvent("1234567887654321");
        eventHandlerService.handle(event);
        verify(accountRepository).deleteById(anyString());
    }

    @Test
    void testHandleAccountCreditedEvent() {
        AccountCreditedEvent event = new AccountCreditedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.ACTIVATED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.ZERO).lastUpdate(LocalDateTime.now())
                .build();
        Operation operation = Operation.builder().account(account).amount(event.getAmount()).type(OperationType.CREDIT)
                .description(event.getDescription()).dateTime(event.getDateTime()).id(UUID.randomUUID().toString())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn( Account.builder().currency(Currency.CAD).id(event.getId())
                .status(AccountStatus.ACTIVATED).customerId("CustomerId").lastUpdate(LocalDateTime.now())
                .creation(LocalDateTime.now()).balance(BigDecimal.TEN)
                .build()
        );
        when(operationRepository.save(any())).thenReturn(operation);

        eventHandlerService.handle(event);
        verify(accountRepository).findById(anyString());
        verify(accountRepository).save(any());
        verify(operationRepository).save(any());
    }

    @Test
    void testHandleAccountCreditedEventThrowAccountNotFoundException() {
        AccountCreditedEvent event = new AccountCreditedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
        verify(operationRepository, never()).save(any());
    }

    @Test
    void testHandleAccountCreditedEventThrowAccountNotActivatedException() {
        AccountCreditedEvent event = new AccountCreditedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.SUSPENDED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.ZERO).lastUpdate(LocalDateTime.now())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        assertThrows(AccountNotActivatedException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
        verify(operationRepository, never()).save(any());
    }

    @Test
    void testHandleAccountDebitedEvent() {
        AccountDebitedEvent event = new AccountDebitedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.ACTIVATED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.TEN).lastUpdate(LocalDateTime.now())
                .build();
        Operation operation = Operation.builder().account(account).amount(event.getAmount()).type(OperationType.CREDIT)
                .description(event.getDescription()).dateTime(event.getDateTime()).id(UUID.randomUUID().toString())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn( Account.builder().currency(Currency.CAD).id(event.getId())
                .status(AccountStatus.ACTIVATED).customerId("CustomerId").lastUpdate(LocalDateTime.now())
                .creation(LocalDateTime.now()).balance(BigDecimal.ZERO)
                .build()
        );
        when(operationRepository.save(any())).thenReturn(operation);

        eventHandlerService.handle(event);
        verify(accountRepository).findById(anyString());
        verify(accountRepository).save(any());
        verify(operationRepository).save(any());
    }

    @Test
    void testHandleAccountDebitedEventThrowAccountNotFoundException() {
        AccountDebitedEvent event = new AccountDebitedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
        verify(operationRepository, never()).save(any());
    }

    @Test
    void testHandleAccountDebitedEventThrowAccountNotActivatedException() {
        AccountDebitedEvent event = new AccountDebitedEvent("1234567887654321", "CREDIT", BigDecimal.TEN, LocalDateTime.now());
        Account account = Account.builder().currency(Currency.CAD).id(event.getId()).status(AccountStatus.SUSPENDED)
                .customerId("CustomerId").creation(LocalDateTime.now()).balance(BigDecimal.TEN).lastUpdate(LocalDateTime.now())
                .build();

        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        assertThrows(AccountNotActivatedException.class, () -> eventHandlerService.handle(event));
        verify(accountRepository, never()).save(any());
        verify(operationRepository, never()).save(any());
    }
}