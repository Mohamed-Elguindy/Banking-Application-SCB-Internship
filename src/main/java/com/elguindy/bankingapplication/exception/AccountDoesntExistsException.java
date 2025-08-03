package com.elguindy.bankingapplication.exception;


import com.elguindy.bankingapplication.util.AccountUtil;

public class AccountDoesntExistsException extends RuntimeException {
    String errorCode;
    String errorMessage;


    public AccountDoesntExistsException() {
        this.errorCode = AccountUtil.Account_Doesnt_Exists_Code;
        this.errorMessage = AccountUtil.Account_Doesnt_Exists_Message;
    }

}
