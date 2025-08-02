package com.elguindy.bankingapplication.controller;

import com.elguindy.bankingapplication.dto.*;
import com.elguindy.bankingapplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Create new Account")
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }


    @GetMapping("/balanceEnquiry")
    public BankResponse BalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("/deposit")
    public BankResponse deposit(@RequestBody DepositRequest depositRequest) {
        return userService.deposit(depositRequest);
    }

    @PostMapping("/withdraw")
    public BankResponse withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        return userService.withdraw(withdrawRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

}
