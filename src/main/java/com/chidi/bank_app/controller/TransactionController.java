package com.chidi.bank_app.controller;


import com.chidi.bank_app.entity.Transaction;
import com.chidi.bank_app.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@CrossOrigin(origins = {"${frontend-url}"})
public class TransactionController {


    @Autowired
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);

    }

}
