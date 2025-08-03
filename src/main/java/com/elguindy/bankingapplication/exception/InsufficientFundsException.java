package com.elguindy.bankingapplication.exception;

import com.elguindy.bankingapplication.util.AccountUtil;

public class InsufficientFundsException extends RuntimeException {
    String errorCode;
    String errorMessage;
    public InsufficientFundsException() {
        this.errorCode = AccountUtil.Insufficient_Funds_Code;
        this.errorMessage = AccountUtil.Insufficient_Funds_Message;
    }
}
