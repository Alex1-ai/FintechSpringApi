package com.chidi.bank_app.service.impl;

import com.chidi.bank_app.dto.TransactionDto;
import com.chidi.bank_app.entity.Transaction;
import com.chidi.bank_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction= Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")

                .build();

        transactionRepository.save(transaction);
        System.out.println("Transaction saved successful");
    }
}
