package com.eyupaydin.service;

import com.eyupaydin.dto.AccountDto;
import com.eyupaydin.model.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AccountServices {

    //CRUD
    public List<AccountDto> getAllAccounts();
    public AccountDto createAccount(AccountDto accountDto);
    public ResponseEntity<AccountDto> getAccountById(Long id);
    public ResponseEntity<AccountDto> updateAccount(Long id, AccountDto accountDto);
    public ResponseEntity<Map<String, Boolean>> deleteAccount(Long id);

    //Model Mapper
    public AccountDto EntityToDto(Account account);
    public Account DtoToEntity(AccountDto accountDto);


}
