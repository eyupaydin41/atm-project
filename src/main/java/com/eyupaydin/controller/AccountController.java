package com.eyupaydin.controller;

import com.eyupaydin.dto.AccountDto;
import com.eyupaydin.model.Account;
import com.eyupaydin.service.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    public final AccountServices accountService;

    public AccountController(AccountServices accountService) {
        this.accountService = accountService;
    }


    //http://localhost:8080/api/accounts
    @GetMapping("/accounts")
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/accounts")
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);
    }

}
