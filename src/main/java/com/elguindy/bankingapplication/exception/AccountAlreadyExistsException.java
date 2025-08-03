package com.elguindy.bankingapplication.exception;

import com.elguindy.bankingapplication.util.AccountUtil;

public class AccountAlreadyExistsException extends RuntimeException {
  String errorCode;
  String errorMessage;
    public AccountAlreadyExistsException() {
      this.errorCode = AccountUtil.Account_Exists_Code;
      this.errorMessage = AccountUtil.Account_Exists_Message;
    }
}
