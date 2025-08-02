package com.elguindy.bankingapplication.repository;

import com.elguindy.bankingapplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {


}
