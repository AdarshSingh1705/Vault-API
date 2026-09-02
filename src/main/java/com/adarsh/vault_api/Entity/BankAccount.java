package com.adarsh.vault_api.Entity;

import com.adarsh.vault_api.Util.BalanceEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users owner;

    @Column(unique = true, nullable = false, length = 10)
    private String accountNumber;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Convert(converter = BalanceEncryptor.class)
    @Column(nullable = false, columnDefinition = "TEXT", precision = 15, scale = 2)
    private BigDecimal balance;
}