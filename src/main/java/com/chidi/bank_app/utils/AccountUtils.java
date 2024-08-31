package com.chidi.bank_app.utils;

import java.time.Year;

public class AccountUtils {


    public static final String ACCOUNT_EXISTS_CODE= "001";

    public static final String ACCOUNT_EXISTS_MESSAGE="This user alreaddy have an account created";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";

    public static final String ACCOUNT_NOT_EXISTS_CODE= "003";

    public static final String ACCOUNT_NOT_EXISTS_MESSAGE="User with provided Account Number does not exists";
    public static final String ACCOUNT_FOUND_CODE = "004";

    public static final String ACCOUNT_FOUND_SUCCESS="User Account Found";

    public static final String ACCOUNT_CREDITED_SUCCESS = "005";

    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="User Account Found";

    public static final String INSUFFICIENT_BALANCE_CODE="006";

    public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient balance";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE="007";

    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE="Account has been successfully debited";

    public static final String TRANSFER_SUCCESS_CODE="008";

    public static final String TRANSFER_SUCCESS_MESSAGE="Transfer Successful";


    public static String generateAccountNumber () {
        /**
         * 2023 * randomSixDigits
         * */

        Year currentYear = Year.now();
        int min = 100000;

        int max = 999999;


        // generate a ranom number between min and max

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        // convert and randomNumber the current random number to string

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        return year + randomNumber;

    }
}
