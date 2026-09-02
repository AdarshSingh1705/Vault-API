package com.adarsh.vault_api.Controller;

import com.adarsh.vault_api.DTO.AccountRequest;
import com.adarsh.vault_api.DTO.AccountResponse;
import com.adarsh.vault_api.Service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        AccountResponse response = accountService.getAccount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        List<AccountResponse> accounts = accountService.getMyAccounts();
        return ResponseEntity.ok(accounts);
    }
}