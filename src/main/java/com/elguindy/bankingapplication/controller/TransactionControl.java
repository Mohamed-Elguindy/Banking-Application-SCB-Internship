package com.elguindy.bankingapplication.controller;
import com.elguindy.bankingapplication.entity.Transaction;
import com.elguindy.bankingapplication.service.BankStatement;
import com.elguindy.bankingapplication.service.BankStatementImp;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor

public class TransactionControl {
    private BankStatement bankStatement;


    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber);
    }

}
