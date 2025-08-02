package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.EmailDetails;
import com.elguindy.bankingapplication.entity.Transaction;
import com.elguindy.bankingapplication.entity.User;
import com.elguindy.bankingapplication.repository.TransactionRepository;
import com.elguindy.bankingapplication.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BankStatementImp implements BankStatement {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceImp emailServiceImp;

    @Override
    public List<Transaction> generateStatement(String accountNumber) throws DocumentException {
        try {
            User currentUser = userRepository.findByAccountNumber(accountNumber);
            if (currentUser == null) {
                throw new IllegalArgumentException("Account not found: " + accountNumber);
            }

            String customerName = currentUser.getFirstName() + " " + currentUser.getLastName();
            List<Transaction> transactionList = transactionRepository.findAll()
                    .stream()
                    .filter(t -> t.getAccountNumber().equals(accountNumber))
                    .collect(Collectors.toList());

            String FILE = System.getProperty("user.home") + "/Documents/BankStatement_" + accountNumber + ".pdf";
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            OutputStream outputStream = new FileOutputStream(FILE);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

            // Bank Header
            PdfPTable bankInfo = new PdfPTable(1);
            PdfPCell bankTitle = new PdfPCell(new Phrase("Suez Canal Bank", titleFont));
            bankTitle.setBackgroundColor(BaseColor.BLUE);
            bankTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
            bankTitle.setBorder(Rectangle.NO_BORDER);
            bankTitle.setPadding(20f);
            bankInfo.addCell(bankTitle);

            PdfPCell bankLocation = new PdfPCell(new Phrase("Egypt", normalFont));
            bankLocation.setHorizontalAlignment(Element.ALIGN_CENTER);
            bankLocation.setBorder(Rectangle.NO_BORDER);
            bankLocation.setPaddingBottom(20f);
            bankInfo.addCell(bankLocation);

            document.add(bankInfo);

            // Account Info
            PdfPTable accountInfo = new PdfPTable(2);
            accountInfo.setWidthPercentage(100);
            accountInfo.setSpacingBefore(20f);
            accountInfo.setSpacingAfter(10f);

            accountInfo.addCell(createLabelCell("Account Holder:", sectionFont));
            accountInfo.addCell(createValueCell(customerName, normalFont));

            accountInfo.addCell(createLabelCell("Account Number:", sectionFont));
            accountInfo.addCell(createValueCell(accountNumber, normalFont));

            accountInfo.addCell(createLabelCell("Email:", sectionFont));
            accountInfo.addCell(createValueCell(currentUser.getEmail(), normalFont));

            document.add(accountInfo);

            // Transactions Table (2 columns only: Type and Amount)
            PdfPTable txnTable = new PdfPTable(2);
            txnTable.setWidthPercentage(100);
            txnTable.setSpacingBefore(10f);
            txnTable.setWidths(new float[]{2, 2});

            txnTable.addCell(createHeaderCell("Transaction Type", headerFont));
            txnTable.addCell(createHeaderCell("Amount", headerFont));

            boolean alternate = false;
            for (Transaction txn : transactionList) {
                BaseColor bg = alternate ? new BaseColor(230, 230, 250) : BaseColor.WHITE;

                String type = txn.getTransactionType() != null ? txn.getTransactionType() : "N/A";
                String amount = txn.getAmount() != null ? txn.getAmount().toString() : "0.00";

                txnTable.addCell(createBodyCell(type, normalFont, bg));
                txnTable.addCell(createBodyCell(amount, normalFont, bg));

                alternate = !alternate;
            }

            document.add(txnTable);

            // Footer Message
            Paragraph thanks = new Paragraph("Thank you for banking with Suez Canal Bank.", sectionFont);
            thanks.setSpacingBefore(30f);
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
            log.info("PDF generated successfully: {}", FILE);

            // Send Email
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(currentUser.getEmail())
                    .subject("Statement of Account")
                    .messageBody("Dear " + customerName + ",\n\nPlease find attached your bank statement.")
                    .attachment(FILE)
                    .build();

            emailServiceImp.sendEmailwithAttachment(emailDetails);
            return transactionList;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF statement: " + e.getMessage(), e);
        }
    }

    // Reusable styling helpers
    private PdfPCell createLabelCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createValueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10f);
        return cell;
    }

    private PdfPCell createBodyCell(String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(8f);
        return cell;
    }
}
