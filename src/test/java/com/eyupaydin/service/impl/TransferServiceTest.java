package com.eyupaydin.service.impl;

import com.eyupaydin.dto.TransferDto;
import com.eyupaydin.model.Account;
import com.eyupaydin.model.Transfer;
import com.eyupaydin.repository.AccountRepository;
import com.eyupaydin.repository.TransferRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransferServiceTest {


    @Mock
    private TransferRepository transferRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TransferServiceImpl transferService;


    @Test
    @DisplayName(value = "Should Return Success Message when Withdraw Money With Valid Amount And Valid Balance")
    void shouldReturnSuccessMessage_whenWithdrawMoneyWithValidAmountAndValidBalance() {
        Account account1 = new Account(4L, "hesap4", 500.0);
        TransferDto transferDto = new TransferDto("hesap4", null, 150.0);

        Transfer transfer = new Transfer(1L, "hesap4", null, 150.0, new Date(1L));

        when(accountRepository.findByAccountNumber("hesap4")).thenReturn(Optional.of(account1));
        when(transferRepository.save(transfer)).thenReturn(transfer);

        ResponseEntity<Map<String, Boolean>> result = transferService.withdrawMoney(transferDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("success, current balance: " + account1.getBalance(), Objects.requireNonNull(result.getBody()).keySet().iterator().next());
        assertTrue(result.getBody().get(result.getBody().keySet().iterator().next()));
    }

    @Test
    @DisplayName(value = "Should Return Fail Message when Withdraw Money With Valid Amount But Insufficient Funds")
    void shouldReturnFailMessage_whenWithdrawMoneyWithInsufficientFunds() {

        Account account1 = new Account(4L, "hesap4", 100.0);
        TransferDto transferDto = new TransferDto("hesap4", null, 150.0);

        when(accountRepository.findByAccountNumber("hesap4")).thenReturn(Optional.of(account1));

        ResponseEntity<Map<String, Boolean>> result = transferService.withdrawMoney(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("insufficient funds", Objects.requireNonNull(result.getBody()).keySet().iterator().next());
        assertFalse(result.getBody().get(result.getBody().keySet().iterator().next()));

        verify(transferRepository, never()).save(any());

    }

    @Test
    @DisplayName(value = "Should Return Fail Message When Withdraw Money With Invalid Amount")
    void shouldReturnFailMessage_whenWithdrawMoneyWithInvalidAmount() {

        Account account1 = new Account(4L, "hesap4", 500.0);
        TransferDto transferDto = new TransferDto("hesap4", null, -150.0);

        when(accountRepository.findByAccountNumber("hesap4")).thenReturn(Optional.of(account1));

        ResponseEntity<Map<String, Boolean>> result = transferService.withdrawMoney(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("invalid amount", Objects.requireNonNull(result.getBody()).keySet().iterator().next());
        assertFalse(result.getBody().get(result.getBody().keySet().iterator().next()));

        verify(transferRepository, never()).save(any());

    }

    @Test
    @DisplayName("should Return Not Found when Withdraw Money With Account Not Present")
    void shouldReturnNotFound_whenWithdrawMoneyWithAccountNotPresent() {

        TransferDto transferDto = new TransferDto("account5", null, 150.0);
        when(accountRepository.findByAccountNumber("account5")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Boolean>> result = transferService.withdrawMoney(transferDto);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(transferRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should Return Success Message when Deposit Money With Valid Amount")
    void shouldReturnSuccessMessage_whenDepositMoneyWithValidAmount() {
        TransferDto transferDto = new TransferDto(null, "account10",500.0);
        Account account = new Account(5L,"account10",200.0);
        when(accountRepository.findByAccountNumber("account10")).thenReturn(Optional.of(account));

        ResponseEntity<Map<String, Boolean>> result = transferService.depositMoney(transferDto);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals("success, current balance: " + account.getBalance(), Objects.requireNonNull(result.getBody()).keySet().iterator().next());
        assertTrue(result.getBody().get(result.getBody().keySet().iterator().next()));


        verify(accountRepository, new Times(1)).save(any());
        verify(transferRepository, new Times(1)).save(any());
    }

    @Test
    @DisplayName("Should Return Fail Message when Deposit Money With Invalid Amount")
    void shouldReturnFailMessage_whenDepositMoneyWithInvalidAmount() {
        TransferDto transferDto = new TransferDto(null, "account10",-100.0);
        Account account = new Account(5L,"account10",200.0);
        when(accountRepository.findByAccountNumber("account10")).thenReturn(Optional.of(account));

        ResponseEntity<Map<String, Boolean>> result = transferService.depositMoney(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
        assertEquals("invalid amount", Objects.requireNonNull(result.getBody()).keySet().iterator().next());
        assertFalse(result.getBody().get(result.getBody().keySet().iterator().next()));

        verify(accountRepository, never()).save(any());
        verify(transferRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should Return Fail Message when Deposit Money With Account Not Present")
    void shouldReturnFailMessage_whenDepositMoneyWithAccountNotPresent() {
        TransferDto transferDto = new TransferDto(null, "account10",100.0);
        when(accountRepository.findByAccountNumber("account10")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Boolean>> result = transferService.depositMoney(transferDto);

        assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());

        verify(accountRepository, never()).save(any());
        verify(transferRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should Return Success Message when Transfer With Valid Balance And Valid Amount")
    void shouldReturnSuccessMessage_whenTransferWithValidBalanceAndValidAmount() {
        Account account1 = new Account(5L,"account5",500.0);
        Account account2 = new Account(6L,"account6",500.0);
        TransferDto transferDto = new TransferDto("account5","account6",150.0);


        when(accountRepository.findByAccountNumber("account5")).thenReturn(Optional.of(account1));
        when(accountRepository.findByAccountNumber("account6")).thenReturn(Optional.of(account2));

        ResponseEntity<Map<TransferDto, Boolean>> result = transferService.transfer(transferDto);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(transferDto, Objects.requireNonNull(result.getBody()).keySet().iterator().next());

        verify(transferRepository, new Times(1)).save(any());
    }

    @Test
    @DisplayName("should Return Fail Message when Transfer With Invalid Balance")
    void shouldReturnFailMessage_whenTransferWithInvalidBalance() {
        Account account1 = new Account(5L,"account5",500.0);
        Account account2 = new Account(6L,"account6",500.0);
        TransferDto transferDto = new TransferDto("account5","account6",600.0);

        when(accountRepository.findByAccountNumber("account5")).thenReturn(Optional.of(account1));
        when(accountRepository.findByAccountNumber("account6")).thenReturn(Optional.of(account2));

        ResponseEntity<Map<TransferDto, Boolean>> result = transferService.transfer(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        verify(transferRepository, never()).save(any());
    }

    @Test
    @DisplayName("should Return Fail Message when Transfer With Invalid Amount")
    void shouldReturnFailMessage_whenTransferWithInvalidAmount() {
        TransferDto transferDto = new TransferDto("account5","account6",-150.0);

        ResponseEntity<Map<TransferDto, Boolean>> result = transferService.transfer(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

        verify(transferRepository, never()).save(any());
        verify(accountRepository, never()).findByAccountNumber(any());
    }

    @Test
    @DisplayName("should Return Fail Message when Transfer With Account Not Present")
    void shouldReturnFailMessage_whenTransferWithAccountNotPresent() {
        TransferDto transferDto = new TransferDto("account5","account6",150.0);


        when(accountRepository.findByAccountNumber("account5")).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber("account6")).thenReturn(Optional.empty());

        ResponseEntity<Map<TransferDto, Boolean>> result = transferService.transfer(transferDto);

        assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());

        verify(transferRepository, never()).save(any());
    }

    @Test
    @DisplayName("should Return Transfer Entity when Convert TransferDto")
    void shouldReturnTransferEntity_whenConvertTransferDto() {
        TransferDto transferDto = new TransferDto("account10","account11", 150.0);
        Transfer expected = new Transfer(1L,"account10","account11",150.0,new Date(0));
        when(modelMapper.map(transferDto,Transfer.class)).thenReturn(expected);

        Transfer actual = transferService.DtoToEntity(transferDto);

        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("should Return TransferDto when Convert TransferEntity")
    void shouldReturnTransferDto_whenConvertTransferEntity() {
        Transfer transfer = new Transfer(1L,"account10","account11",150.0,new Date(0));
        TransferDto expected = new TransferDto("account10","account11", 150.0);
        when(modelMapper.map(transfer,TransferDto.class)).thenReturn(expected);

        TransferDto actual = transferService.EntityToDto(transfer);

        assertEquals(expected,actual);
    }



}