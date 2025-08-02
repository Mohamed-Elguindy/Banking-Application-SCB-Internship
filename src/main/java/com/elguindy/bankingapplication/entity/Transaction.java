package com.elguindy.bankingapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name ="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String AccountNumber;
    private String status;


}
