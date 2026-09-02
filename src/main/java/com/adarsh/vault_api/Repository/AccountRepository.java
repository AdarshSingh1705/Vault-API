package com.adarsh.vault_api.Repository;

import com.adarsh.vault_api.Entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByOwner_Email (String email);
}
