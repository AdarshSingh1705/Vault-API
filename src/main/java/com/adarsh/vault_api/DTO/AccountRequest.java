package com.adarsh.vault_api.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial deposit must be greater than 0")
    private BigDecimal initialDeposit;
}