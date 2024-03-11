package com.eyupaydin.service.impl;

import com.eyupaydin.dto.TransferDto;
import com.eyupaydin.model.Account;
import com.eyupaydin.model.Transfer;
import com.eyupaydin.repository.AccountRepository;
import com.eyupaydin.repository.TransferRepository;
import com.eyupaydin.service.TransferServices;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferServices {

    AccountRepository accountRepository;
    TransferRepository transferRepository;
    ModelMapper modelMapper;

    public TransferServiceImpl(AccountRepository accountRepository, TransferRepository transferRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> withdrawMoney(TransferDto transferDto) {

        if (transferDto.getAmount() > 0) {

            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(transferDto.getSenderAccNo());

            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                if (account.getBalance() >= transferDto.getAmount()) {
                    account.setBalance(account.getBalance() - transferDto.getAmount());
                    accountRepository.save(account);

                    Map<String, Boolean> response = new HashMap<>();
                    response.put("success, current balance: " + account.getBalance(), true);
                    transferRepository.save(DtoToEntity(transferDto));
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Boolean> response = new HashMap<>();
                    response.put("insufficient funds", false);
                    return ResponseEntity.badRequest().body(response);
                }
            }

            return ResponseEntity.notFound().build();
        } else {
            Map<String, Boolean> response = new HashMap<>();
            response.put("invalid amount", false);
            return ResponseEntity.badRequest().body(response);
        }

    }

    @Override
    public ResponseEntity<Map<String, Boolean>> depositMoney(TransferDto transferDto) {

        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(transferDto.getReceiverAccNo());

        if (transferDto.getAmount() > 0.0) {
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                account.setBalance(account.getBalance() + transferDto.getAmount());
                accountRepository.save(account);

                Map<String, Boolean> response = new HashMap<>();
                response.put("success, current balance: " + account.getBalance(), true);

                transferRepository.save(DtoToEntity(transferDto));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } else {
            Map<String, Boolean> response = new HashMap<>();
            response.put("invalid amount", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<TransferDto, Boolean>> transfer(TransferDto transferDto) {
        if (transferDto.getAmount() > 0) {

            Optional<Account> optionalSenderAcc = accountRepository.findByAccountNumber(transferDto.getSenderAccNo());
            Optional<Account> optionalReceiverAcc = accountRepository.findByAccountNumber(transferDto.getReceiverAccNo());

            if (optionalSenderAcc.isPresent() && optionalReceiverAcc.isPresent()) {
                Account senderAccount = optionalSenderAcc.get();
                Account receiverAccount = optionalReceiverAcc.get();

                if (senderAccount.getBalance() >= transferDto.getAmount()) {

                    senderAccount.setBalance(senderAccount.getBalance() - transferDto.getAmount());
                    receiverAccount.setBalance(receiverAccount.getBalance() + transferDto.getAmount());

                    Map<TransferDto, Boolean> response = new HashMap<>();
                    response.put(transferDto, true);

                    transferRepository.save(DtoToEntity(transferDto));

                    return ResponseEntity.ok(response);
                } else { // insufficient funds
                    Map<TransferDto,Boolean> badRequestResponse = new HashMap<>();
                    badRequestResponse.put(new TransferDto(),false);

                    return ResponseEntity.badRequest().body(badRequestResponse);
                }
            }
        } else { // invalid Amount
            Map<TransferDto,Boolean> badRequestResponse = new HashMap<>();
            badRequestResponse.put(new TransferDto(),false);

            return ResponseEntity.badRequest().body(badRequestResponse);
        }
        return ResponseEntity.notFound().build();
    }

    public Transfer DtoToEntity(TransferDto transferDto) {
        return modelMapper.map(transferDto, Transfer.class);
    }

    public TransferDto EntityToDto(Transfer transfer) {
        return modelMapper.map(transfer, TransferDto.class);
    }


}
