package com.elguindy.bankingapplication.util;

import java.time.Year;

public class AccountUtil {




    public static String genrateAccountNumber() {
        Year currentYear = Year.now();
        int randNum = (int)Math.floor(Math.random()*100)+1;
        String randnum = String.valueOf(randNum);
        String currentYearString = currentYear.toString();
        return currentYearString + randnum;

    }
    public static final String Account_Exists_Code = "001";
    public static final String Account_Exists_Message = "Account Already Exists";

    public static final String Account_Created_Successfully_Code = "002";
    public static final String Account_Created_Successfully_Message = "Account Created Successfully";

    public static final String Account_Doesnt_Exists_Code = "003";
    public static final String Account_Doesnt_Exists_Message = "Account doesn't Exists";

    public static final String Account_Found_Code = "004";
    public static final String Account_Found_Message = "Account Found Successfully";

    public static final String Deposit_Done_Successfully_Code = "005";
    public static final String Deposit_Done_Successfully_Message = "Deposit Successfully Done";

    public static final String Withdraw_Done_Successfully_Code = "006";
    public static final String Withdraw_Done_Successfully_Message = "Withdraw Successfully Done";

    public static final String Insufficient_Funds_Code = "007";
    public static final String Insufficient_Funds_Message = "Insufficient Funds";

    public static final String Transfer_Done_Successfully_Code = "008";
    public static final String Transfer_Done_Successfully_Message = "Transfer Successfully Done";







}
