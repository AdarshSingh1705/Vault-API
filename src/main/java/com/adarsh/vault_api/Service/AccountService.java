package com.adarsh.vault_api.Service;

import com.adarsh.vault_api.DTO.AccountRequest;
import com.adarsh.vault_api.DTO.AccountResponse;
import com.adarsh.vault_api.Entity.BankAccount;
import com.adarsh.vault_api.Entity.Users;
import com.adarsh.vault_api.Exception.UnauthorizedAccessException;
import com.adarsh.vault_api.Repository.AccountRepository;
import com.adarsh.vault_api.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,
                          AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        // Get authenticated user's email from SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate unique account number
        String accountNumber = accountNumberGenerator.generateUniqueAccountNumber();

        // Create account
        BankAccount account = new BankAccount();
        account.setOwner(user);
        account.setAccountNumber(accountNumber);
        account.setBalance(request.getInitialDeposit());

        BankAccount savedAccount = accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    public AccountResponse getAccount(Long accountId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // BOLA Prevention: Check ownership
        if (!account.getOwner().getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to access this account");
        }

        return mapToResponse(account);
    }

    public List<AccountResponse> getMyAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<BankAccount> accounts = accountRepository.findByOwner_Email(email);

        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse mapToResponse(BankAccount account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}