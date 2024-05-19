package com.brodygaudel.accountservice.command.service.implementation;

import com.brodygaudel.accountservice.command.dto.*;
import com.brodygaudel.accountservice.command.feign.CustomerRestClient;
import com.brodygaudel.accountservice.command.model.*;
import com.brodygaudel.accountservice.command.util.IdGenerator;
import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;
import com.brodygaudel.accountservice.common.enums.Sex;
import com.brodygaudel.accountservice.common.exception.CustomerNotFoundException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
class AccountCommandServiceImplTest {

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private CustomerRestClient customerRestClient;

    @InjectMocks
    private AccountCommandServiceImpl commandService;

    @BeforeEach
    void setUp() {
        commandService = new AccountCommandServiceImpl(commandGateway, idGenerator, customerRestClient);
    }

    @Test
    void testCreate() {
        String id = UUID.randomUUID().toString();
        CreateAccountDTO dto = new CreateAccountDTO(id, Currency.TND);
        CustomerDTO customerDTO = new CustomerDTO(id, "cin", "john", "doe", LocalDate.of(2000,1,1),
                "world", "Gabon", Sex.M, "email@gmail.com", LocalDateTime.now(), null
        );
        String accountId = "1234567887654321";
        CreateAccountCommand command = new CreateAccountCommand(
                accountId, AccountStatus.CREATED, BigDecimal.ZERO, dto.currency(), dto.customerId(), LocalDateTime.now()
        );

        when(customerRestClient.getById(anyString())).thenReturn(customerDTO);
        when(idGenerator.autoGenerate()).thenReturn("accountId");
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("accountId"));

        // Act
        CompletableFuture<String> future = commandService.create(dto);

        // Assert
        assertNotNull(future);
        assertEquals("accountId", future.join());
        verify(commandGateway).send(any(CreateAccountCommand.class));
    }

    @Test
    void testCreateThrowsCustomerNotFoundException() {
        // Arrange
        String id = UUID.randomUUID().toString();
        CreateAccountDTO dto = new CreateAccountDTO(id, Currency.TND);
        when(customerRestClient.getById(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> commandService.create(dto));
        verify(commandGateway, never()).send(any());
    }

    @Test
    void testUpdateStatusActivated() {
        // Arrange
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO("accountId", AccountStatus.ACTIVATED);
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("Success"));

        // Act
        CompletableFuture<String> future = commandService.update(updateStatusDTO);

        // Assert
        assertNotNull(future);
        assertEquals("Success", future.join());
        verify(commandGateway).send(any(ActiveAccountCommand.class));
        verify(commandGateway, never()).send(any(SuspendAccountCommand.class));
    }

    @Test
    void testUpdateStatusNotActivated(){
        // Arrange
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO("accountId", AccountStatus.SUSPENDED);
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("Success"));

        // Act
        CompletableFuture<String> future = commandService.update(updateStatusDTO);

        // Assert
        assertNotNull(future);
        assertEquals("Success", future.join());
        verify(commandGateway, never()).send(any(ActiveAccountCommand.class));
        verify(commandGateway).send(any(SuspendAccountCommand.class));
    }



    @Test
    void testCredit() {
        // Arrange
        CreditDTO creditDTO = new CreditDTO("accountId", "Credit Description", BigDecimal.TEN);
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("Success"));

        // Act
        CompletableFuture<String> future = commandService.credit(creditDTO);

        // Assert
        assertNotNull(future);
        assertEquals("Success", future.join());
        verify(commandGateway).send(any(CreditAccountCommand.class));
    }

    @Test
    void testDebit() {
        // Arrange
        DebitDTO debitDTO = new DebitDTO("accountId", "Credit Description", BigDecimal.TEN);
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("Success"));

        // Act
        CompletableFuture<String> future = commandService.debit(debitDTO);

        // Assert
        assertNotNull(future);
        assertEquals("Success", future.join());
        verify(commandGateway).send(any(DebitAccountCommand.class));
    }

    @Test
    void testDelete() {
        // Arrange
        String accountId = "accountId";
        when(commandGateway.send(any())).thenReturn(CompletableFuture.completedFuture("Success"));

        // Act
        CompletableFuture<String> future = commandService.delete(accountId);

        // Assert
        assertNotNull(future);
        assertEquals("Success", future.join());
        verify(commandGateway).send(any(DeleteAccountCommand.class));
    }
}