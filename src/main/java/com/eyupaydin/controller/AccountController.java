package com.eyupaydin.controller;

import com.eyupaydin.dto.AccountDto;
import com.eyupaydin.service.AccountServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    public final AccountServices accountService;

    public AccountController(AccountServices accountService) {
        this.accountService = accountService;
    }


    // http://localhost:8080/api/accounts
    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // http://localhost:8080/api/accounts
    @PostMapping
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    // http://localhost:8080/api/accounts/1
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    // http://localhost:8080/api/accounts/1
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto);
    }

    // http://localhost:8080/api/accounts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);
    }

}
