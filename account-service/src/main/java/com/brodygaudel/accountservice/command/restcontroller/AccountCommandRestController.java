package com.brodygaudel.accountservice.command.restcontroller;

import com.brodygaudel.accountservice.command.dto.CreateAccountDTO;
import com.brodygaudel.accountservice.command.dto.CreditDTO;
import com.brodygaudel.accountservice.command.dto.DebitDTO;
import com.brodygaudel.accountservice.command.dto.UpdateStatusDTO;
import com.brodygaudel.accountservice.command.service.AccountCommandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/commands")
public class AccountCommandRestController {

    private final AccountCommandService accountCommandService;

    @Autowired
    public AccountCommandRestController(AccountCommandService accountCommandService) {
        this.accountCommandService = accountCommandService;
    }

    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountDTO dto) {
        return accountCommandService.create(dto);
    }

    @PutMapping("/update")
    public CompletableFuture<String> updateAccountStatus(@RequestBody UpdateStatusDTO dto) {
        return accountCommandService.update(dto);
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditDTO dto) {
        return accountCommandService.credit(dto);
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitDTO dto) {
        return accountCommandService.debit(dto);
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> deleteAccount(@PathVariable String id) {
        return accountCommandService.delete(id);
    }

}

