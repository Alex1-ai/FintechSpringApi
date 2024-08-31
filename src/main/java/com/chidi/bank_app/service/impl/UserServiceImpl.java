package com.chidi.bank_app.service.impl;

import com.chidi.bank_app.dto.*;
import com.chidi.bank_app.entity.User;
import com.chidi.bank_app.repository.UserRepository;
import com.chidi.bank_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;

    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an account - saving new user in the database
         * CHECK if user alreadu have an account
         * */
        System.out.println("TExt here");
        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
//            return response;
        }
        System.out.println("Checking for erro");
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepository.save(newUser);
        System.out.println("This is the saved user " + savedUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())

                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully created.\nYour Account Details: \n Account Name:  "
                        + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName()
                        + "\nAccount Number: "+ savedUser.getAccountNumber()

                )
                .build();
        emailService.sendEmailAlert(emailDetails);
        System.out.println("ABOUT TO PRINT  SUCCESS");
        System.out.println("success");

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " "+ savedUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // check if the provided account number exists in the db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE )
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(foundUser.getAccountBalance())
                                .accountNumber(request.getAccountNumber())
                                .accountName(foundUser.getFirstName() + " "+ foundUser.getLastName() + " "+ foundUser.getOtherName())
                                .build()
                )
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " "+ foundUser.getLastName() + " "+ foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE )
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()) );
        userRepository.save(userToCredit);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())

                .build();
        transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " "+ userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // check if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
//        System.out.println("1");
        if (!isAccountExist) {
//            System.out.println("2");
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // check if the amount you intend to withdraw is not more than the current balance in your account
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
//        System.out.println("3");

        // Change from Integer to BigDecimal
        BigDecimal availableAmount = userToDebit.getAccountBalance();
        BigDecimal debitAmount = request.getAmount();

        if (availableAmount.compareTo(debitAmount) < 0) {
//            System.out.println("4");
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
//            System.out.println("6");
            userToDebit.setAccountBalance(availableAmount.subtract(debitAmount));
            userRepository.save(userToDebit);
//            System.out.println("7");
            // Save transaction
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())

                    .build();
            transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountNumber(request.getAccountNumber())
                                    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                                    .accountBalance(userToDebit.getAccountBalance())
                                    .build()
                    )
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // get the account to debit (check if it exists
        // check if the amount i'm debiting is not more thant the current balance
        // debit the account
        // get the account to credit
        // credit the account
//        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExist){
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                   .accountInfo(null)
                   .build();
        }

       User sourceAcountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAcountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }


        sourceAcountUser.setAccountBalance(sourceAcountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUsername = sourceAcountUser.getFirstName() + " "+ sourceAcountUser.getLastName() + " " + sourceAcountUser.getOtherName();

        userRepository.save(sourceAcountUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAcountUser.getEmail())
                .messageBody("The sum of "+ request.getAmount() + " has been deducted from your account! Your current account balance is "+ sourceAcountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(sourceAcountUser.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())

                .build();
        transactionService.saveTransaction(transactionDto);
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        String recipientUsername = destinationAccountUser.getFirstName() + " "+ destinationAccountUser.getLastName() + " " + destinationAccountUser.getOtherName();

        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("The sum of "+ request.getAmount() + " has been  sent to your account from " + sourceUsername +  " Your current account balance is "+ destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);


//        // Save transaction
//        TransactionDto transactionDto = TransactionDto.builder()
//                .accountNumber(use)
//                .build();
        // Save transaction
//        TransactionDto transactionDto2 = TransactionDto.builder()
//                .accountNumber(destinationAccountUser.getAccountNumber())
//                .transactionType("CREDIT")
//                .amount(request.getAmount())
//
//                .build();
//        transactionService.saveTransaction(transactionDto2);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }


    // balance Enquiry , name Enquiry , credit, debit , transfer




}
