package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.TransactionsDto;
import jakarta.transaction.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionsDto transactionsDto);
}
