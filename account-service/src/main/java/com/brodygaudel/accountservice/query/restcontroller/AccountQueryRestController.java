package com.brodygaudel.accountservice.query.restcontroller;

import com.brodygaudel.accountservice.query.dto.AccountDTO;
import com.brodygaudel.accountservice.query.dto.OperationDTO;
import com.brodygaudel.accountservice.query.dto.OperationPageDTO;
import com.brodygaudel.accountservice.query.model.GetAccountByCustomerIdQuery;
import com.brodygaudel.accountservice.query.model.GetAccountByIdQuery;
import com.brodygaudel.accountservice.query.model.GetOperationByIdQuery;

import com.brodygaudel.accountservice.query.model.GetOperationsByAccountIdQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/queries")
public class AccountQueryRestController {

    private final QueryGateway queryGateway;

    public AccountQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get-account/{id}")
    public AccountDTO getAccountById(@PathVariable String id){
        GetAccountByIdQuery query = new GetAccountByIdQuery(id);
        ResponseType<AccountDTO> responseType = ResponseTypes.instanceOf(AccountDTO.class);
        return queryGateway.query(query , responseType).join();
    }

    @GetMapping("/find-account/{customerId}")
    public AccountDTO getAccountByCustomerId(@PathVariable String customerId){
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery(customerId);
        ResponseType<AccountDTO> responseType = ResponseTypes.instanceOf(AccountDTO.class);
        return queryGateway.query(query , responseType).join();
    }

    @GetMapping("/get-operation/{id}")
    public OperationDTO getOperationById(@PathVariable String id){
        GetOperationByIdQuery query = new GetOperationByIdQuery(id);
        ResponseType<OperationDTO> responseType = ResponseTypes.instanceOf(OperationDTO.class);
        return queryGateway.query(query , responseType).join();
    }

    @GetMapping("/find-operations")
    public OperationPageDTO getOperationsByAccountId(@RequestParam(name = "accountId", defaultValue = "") String accountId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "10") int size){

        GetOperationsByAccountIdQuery query = new GetOperationsByAccountIdQuery(accountId, page, size);
        ResponseType<List<OperationDTO>> responseType = ResponseTypes.multipleInstancesOf(OperationDTO.class);
        List<OperationDTO> operationDTOS = queryGateway.query(query, responseType).join();
        return new OperationPageDTO(page, size, operationDTOS.size(), operationDTOS);
    }
}
