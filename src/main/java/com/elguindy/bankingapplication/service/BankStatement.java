package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.entity.Transaction;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatement {
     List<Transaction> generateStatement(String accountNumber) throws FileNotFoundException, DocumentException;
}
