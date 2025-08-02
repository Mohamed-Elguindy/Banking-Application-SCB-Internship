package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.TransactionsDto;
import com.elguindy.bankingapplication.entity.Transaction;
import com.elguindy.bankingapplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TransactionImp implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionsDto transactionDto) {
        Transaction  transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .AccountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("Success")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");

    }
}
