package com.elguindy.bankingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionsDto {
    private String transactionType;
    private BigDecimal amount;
    private String AccountNumber;
    private String status;

}
