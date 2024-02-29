package com.eyupaydin.service.impl;

import com.eyupaydin.dto.AccountDto;
import com.eyupaydin.model.Account;
import com.eyupaydin.repository.AccountRepository;
import com.eyupaydin.service.AccountServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountServices {

    AccountRepository accountRepository;
    ModelMapper modelMapper;
    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::EntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = DtoToEntity(accountDto);
        accountRepository.save(account);
        return accountDto;
    }

    @Override
    public ResponseEntity<AccountDto> getAccountById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            AccountDto accountDto = EntityToDto(account);
            return ResponseEntity.ok(accountDto);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<AccountDto> updateAccount(Long id, AccountDto accountDto) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setId(id);
            account.setAccountNumber(accountDto.getAccountNumber());
            account.setBalance(accountDto.getBalance());
            accountRepository.save(account);

            ResponseEntity.ok(EntityToDto(account));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            accountRepository.delete(account);

            Map<String,Boolean> response = new HashMap<>();
            response.put("deleted",true);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public AccountDto EntityToDto(Account account) {
        return modelMapper.map(account,AccountDto.class);
    }

    @Override
    public Account DtoToEntity(AccountDto accountDto) {
        return modelMapper.map(accountDto,Account.class);
    }
}
