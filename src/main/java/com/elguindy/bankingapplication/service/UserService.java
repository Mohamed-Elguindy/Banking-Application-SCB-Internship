package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.*;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest Request);
    BankResponse deposit(DepositRequest depositRequest);
    BankResponse withdraw(WithdrawRequest withdrawRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
