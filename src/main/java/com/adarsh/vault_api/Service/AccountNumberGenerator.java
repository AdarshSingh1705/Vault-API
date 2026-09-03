package com.adarsh.vault_api.Service;

import com.adarsh.vault_api.Repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generateRandomNumber() {

        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }
}