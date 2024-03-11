package com.eyupaydin;

import com.eyupaydin.model.Account;
import com.eyupaydin.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class AtmProjectApplication implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public AtmProjectApplication(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(AtmProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Account a1 = Account.builder()
                .id(1L)
                .accountNumber("account1")
                .balance(500.0)
                .build();

        Account a2 = Account.builder()
                .id(2L)
                .accountNumber("account2")
                .balance(500.0)
                .build();

        accountRepository.saveAll(Arrays.asList(a1,a2));

    }
}
