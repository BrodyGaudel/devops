package com.brodygaudel.accountservice.query.service;

import com.brodygaudel.accountservice.common.enums.AccountStatus;
import com.brodygaudel.accountservice.common.enums.Currency;
import com.brodygaudel.accountservice.common.enums.OperationType;
import com.brodygaudel.accountservice.common.exception.AccountNotFoundException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
class AccountQueryHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private AccountQueryHandlerService queryHandlerService;

    @BeforeEach
    void setUp() {
        queryHandlerService = new AccountQueryHandlerService(accountRepository, operationRepository);
    }

    @Test
    void testHandleGetAccountByIdQuery() {
        GetAccountByIdQuery query = new GetAccountByIdQuery("1234567887654321");
        Account account = Account.builder().balance(BigDecimal.TEN).customerId("customerId").lastUpdate(LocalDateTime.now())
                .currency(Currency.TND).creation(LocalDateTime.now()).status(AccountStatus.ACTIVATED).operations(new ArrayList<>()).id(query.getId())
                .build();
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        AccountDTO accountDTO = queryHandlerService.handle(query);
        assertNotNull(accountDTO);
        assertEquals(query.getId(), accountDTO.getId());
    }

    @Test
    void testHandleGetAccountByIdQueryThrowAccountNotFoundException() {
        GetAccountByIdQuery query = new GetAccountByIdQuery("1234567887654321");
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> queryHandlerService.handle(query));
    }

    @Test
    void testHandleGetAccountByCustomerIdQuery() {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customerId");
        Account account = Account.builder().balance(BigDecimal.TEN).customerId(query.getCustomerId()).lastUpdate(LocalDateTime.now())
                .currency(Currency.TND).creation(LocalDateTime.now()).status(AccountStatus.ACTIVATED).operations(new ArrayList<>())
                .id("1234567887654321")
                .build();
        when(accountRepository.findByCustomerId(anyString())).thenReturn(Optional.of(account));
        AccountDTO accountDTO = queryHandlerService.handle(query);
        assertNotNull(accountDTO);
        assertEquals(query.getCustomerId(), accountDTO.getCustomerId());
    }

    @Test
    void testHandleGetAccountByCustomerIdQueryThrowAccountNotFoundException() {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customerId");
        when(accountRepository.findByCustomerId(anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> queryHandlerService.handle(query));
    }

    @Test
    void testHandleGetOperationByIdQuery() {
        GetOperationByIdQuery query = new GetOperationByIdQuery(UUID.randomUUID().toString());
        Account account = Account.builder().balance(BigDecimal.TEN).customerId("customerId").lastUpdate(LocalDateTime.now())
                .currency(Currency.TND).creation(LocalDateTime.now()).status(AccountStatus.ACTIVATED).operations(new ArrayList<>())
                .id("1234567887654321")
                .build();
        Operation operation = new Operation(query.getId(),LocalDateTime.now(), "CREDIT", OperationType.CREDIT, BigDecimal.TEN,account);
        when(operationRepository.findById(anyString())).thenReturn(Optional.of(operation));
        OperationDTO operationDTO = queryHandlerService.handle(query);
        assertNotNull(operationDTO);
        assertEquals(query.getId(), operationDTO.getId());
    }

    @Test
    void testHandleGetOperationByIdQueryThrowOperationNotFoundException() {
        GetOperationByIdQuery query = new GetOperationByIdQuery(UUID.randomUUID().toString());
        Account account = Account.builder().balance(BigDecimal.TEN).customerId("customerId").lastUpdate(LocalDateTime.now())
                .currency(Currency.TND).creation(LocalDateTime.now()).status(AccountStatus.ACTIVATED).operations(new ArrayList<>())
                .id("1234567887654321")
                .build();
        Operation operation = new Operation(query.getId(),LocalDateTime.now(), "CREDIT", OperationType.CREDIT, BigDecimal.TEN,account);
        when(operationRepository.findById(anyString())).thenReturn(Optional.of(operation));
        OperationDTO operationDTO = queryHandlerService.handle(query);
        assertNotNull(operationDTO);
        assertEquals(query.getId(), operationDTO.getId());
    }

    @Test
    void testHandleGetOperationsByAccountIdQuery(){
        int page = 0;
        int size = 10;
        String accountId = "1234567887654321";
        GetOperationsByAccountIdQuery query = new GetOperationsByAccountIdQuery(accountId, page,size);
        Account account = Account.builder().balance(BigDecimal.TEN).customerId("customerId").lastUpdate(LocalDateTime.now())
                .currency(Currency.TND).creation(LocalDateTime.now()).status(AccountStatus.ACTIVATED).operations(new ArrayList<>())
                .id("1234567887654321")
                .build();
        Operation operation1 = new Operation("1",LocalDateTime.now(), "CREDIT", OperationType.CREDIT, BigDecimal.TEN,account);
        Operation operation2 = new Operation("2",LocalDateTime.now(), "CREDIT", OperationType.CREDIT, BigDecimal.TEN,account);

        Page<Operation> operationPage = new PageImpl<>(List.of(operation1, operation2), PageRequest.of(page, size), 2);
        when(operationRepository.findByAccountId(accountId, PageRequest.of(page,size))).thenReturn(operationPage);
        List<OperationDTO> operationDTOS = queryHandlerService.handle(query);
        assertNotNull(operationDTOS);
        assertFalse(operationDTOS.isEmpty());
        assertEquals(2, operationDTOS.size());
    }
}