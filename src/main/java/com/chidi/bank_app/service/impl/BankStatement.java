package com.chidi.bank_app.service.impl;


import com.chidi.bank_app.dto.EmailDetails;
import com.chidi.bank_app.entity.Transaction;
import com.chidi.bank_app.entity.User;
import com.chidi.bank_app.repository.TransactionRepository;
import com.chidi.bank_app.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service

@Slf4j
public class BankStatement {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    private static final String FILE = "C:\\Users\\alexa\\Documents\\MyStatement.pdf";


    /*
     * retrieve list of transactions within a date range given an account number
     * generate a pdf file of transactions
     *  send file via email
     * */


//
public List<Transaction> generateStatement(
        String accountNumber,
        String startDate,
        String endDate
) throws DocumentException, FileNotFoundException {
    LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
    LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
    List<Transaction> transactionList = transactionRepository
            .findAll().stream()
            .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
            .filter(transaction -> transaction.getCreateAt().isAfter(start) && transaction.getCreateAt().isBefore(end.plusDays(1)))
            .toList();

    User user = userRepository.findByAccountNumber(accountNumber);
    String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();

    Rectangle statementSize = new Rectangle(PageSize.A4);
    Document document = new Document(statementSize);
    log.info("setting size of document");
    OutputStream outputStream = new FileOutputStream(FILE);
    PdfWriter.getInstance(document, outputStream);
    document.open();

    PdfPTable bankInfoTable = new PdfPTable(1);
    PdfPCell bankName = new PdfPCell(new Phrase("PAY-MAGNET"));
    bankName.setBorder(0);
    bankName.setBackgroundColor(BaseColor.BLUE);
    bankName.setPadding(20f);

    PdfPCell bankAddress = new PdfPCell(new Phrase("23 Amuwo, Lagos Nigeria"));
    bankAddress.setBorder(0);
    bankInfoTable.addCell(bankName);
    bankInfoTable.addCell(bankAddress);

    PdfPTable statementInfo = new PdfPTable(2);
    PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
    customerInfo.setBorder(0);

    PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
    statement.setBorder(0);
    PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
    stopDate.setBorder(0);

    PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
    name.setBorder(0);
    PdfPCell space = new PdfPCell();
    space.setBorder(0);

    PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
    address.setBorder(0);

    PdfPTable transactionTable = new PdfPTable(4);
    PdfPCell date = new PdfPCell(new Phrase("DATE"));
    date.setBackgroundColor(BaseColor.BLUE);
    date.setBorder(0);

    PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
    transactionType.setBackgroundColor(BaseColor.BLUE);
    transactionType.setBorder(0);

    PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
    transactionAmount.setBackgroundColor(BaseColor.BLUE);
    transactionAmount.setBorder(0);

    PdfPCell status = new PdfPCell(new Phrase("STATUS"));
    status.setBackgroundColor(BaseColor.BLUE);
    status.setBorder(0);

    transactionTable.addCell(date);
    transactionTable.addCell(transactionType);
    transactionTable.addCell(transactionAmount);
    transactionTable.addCell(status);

    transactionList.forEach(transaction -> {
        transactionTable.addCell(new Phrase(transaction.getCreateAt().toString()));
        transactionTable.addCell(new Phrase(transaction.getTransactionType()));
        transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
        transactionTable.addCell(new Phrase(transaction.getStatus()));
    });

    statementInfo.addCell(customerInfo);
    statementInfo.addCell(statement);
    statementInfo.addCell(stopDate);
    statementInfo.addCell(name);
    statementInfo.addCell(space);
    statementInfo.addCell(address);

    document.add(bankInfoTable);
    document.add(statementInfo);
    document.add(transactionTable);

    document.close();

    EmailDetails emailDetails = EmailDetails.builder()
            .recipient(user.getEmail())
            .subject("STATEMENT OF ACCOUNT")
            .messageBody("Kindly find your requested account statement attached!")
            .attachment(FILE)
            .build();

    emailService.sendEmailWithAttachment(emailDetails);

    return transactionList;
}


    private void designStatement(List<Transaction> transactions) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);

        PdfPCell bankName = new PdfPCell(new Phrase("PAY-MAGNET"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("23 Amuwo, Lagos Nigeria"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
//        PdfPCell customerInfo = new PdfPCell("Start Date: "+);






    }
}

