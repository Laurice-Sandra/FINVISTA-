package esprit.tn.flexifin.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.TranStatus;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.TransactionRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public TransactionService(JavaMailSender mailSender, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.mailSender = mailSender;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.orElse(null);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found for this id :: " + id));
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setStatus(transactionDetails.getStatus());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found for this id :: " + transactionId));
        transactionRepository.delete(transaction);
    }

    public List<Transaction> filterTransactionsByStatusAndDate(TranStatus status, Date date) {
        return transactionRepository.findByStatusAndDate(status, date);
    }

    public Integer calculateSumByStatus(TranStatus status) {
        return transactionRepository.sumTransactionsByStatus(status);
    }

    public Integer calculateSumByDate(Date date) {
        return transactionRepository.sumTransactionsByDate(date);
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
    // Transaction History

    public List<Map<String, Object>> getSimplifiedTransactionHistory(Long accountId) {
        List<Transaction> transactions = getTransactionHistory(accountId);
        return transactions.stream().map(transaction -> {
            Map<String, Object> transactionInfo = new HashMap<>();
            transactionInfo.put("idTransaction", transaction.getIdTransaction());
            transactionInfo.put("amount", transaction.getAmount());
            transactionInfo.put("date", transaction.getDate());
            transactionInfo.put("status", transaction.getStatus().toString());
            return transactionInfo;
        }).collect(Collectors.toList());
    }

    public Transaction recordTransaction(String sessionId, Long accountId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account does not exist"));

        Transaction transaction = new Transaction();
        transaction.setAmount(Math.toIntExact(session.getAmountTotal() / 100));
        transaction.setDate(new Date());
        transaction.setStatus(TranStatus.COMPLETED);
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    public String createStripeSession(Long accountId) throws StripeException {
        String successUrl = "http://localhost:8085/api/transactions/payment-success?session_id={CHECKOUT_SESSION_ID}&account_id=" + accountId;
        String cancelUrl = "http://localhost:8085/api/transactions/payment-cancelled";

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setCustomerEmail("khalil.nacef.kn@gmail.com")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(2000L)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Transaction Payment")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    public String processPaymentSuccess(String sessionId, Long accountId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        String customerEmail = "khalil.nacef.kn@gmail.com";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(customerEmail);
        email.setFrom("khalil.nacef.kn@gmail.com");
        email.setSubject("Payment Confirmation");
        email.setText("Your payment has been successfully processed. Thank you for your purchase.");
        mailSender.send(email);

        recordTransaction(sessionId, accountId);
        return "The payment was successfully processed and the transaction recorded.";
    }

    public String processPaymentCancelled() {
        return "The payment was cancelled.";
    }

    // 2 accounts transaction
    public String transferFunds(Long fromAccountId, Long toAccountId, Float amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));


        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds in the sender's account");
        }


        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);


        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);


        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(-amount.intValue());
        debitTransaction.setDate(new Date());
        debitTransaction.setStatus(TranStatus.COMPLETED);
        debitTransaction.setAccount(fromAccount);
        transactionRepository.save(debitTransaction);


        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount.intValue());
        creditTransaction.setDate(new Date());
        creditTransaction.setStatus(TranStatus.COMPLETED);
        creditTransaction.setAccount(toAccount);
        transactionRepository.save(creditTransaction);

        return "Funds transferred successfully";
    }

    //Excel
    public ByteArrayInputStream generateFlexibleReportExcel(List<Long> accountIds, List<Integer> years, List<Integer> months) throws IOException {

        if (years == null) years = new ArrayList<>();
        if (months == null) months = new ArrayList<>();

        LocalDate startDate;
        LocalDate endDate;
        if (!years.isEmpty() && !months.isEmpty()) {
            startDate = LocalDate.of(Collections.min(years), Collections.min(months), 1);
            endDate = LocalDate.of(Collections.max(years), Collections.max(months), startDate.withMonth(Collections.max(months)).lengthOfMonth());
        } else {
            startDate = LocalDate.of(1900, 1, 1);
            endDate = LocalDate.now();
        }

        List<Transaction> transactions = transactionRepository.findByAccountIdsAndDateRange(
                (accountIds == null || accountIds.isEmpty()) ? null : accountIds, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transactions Report");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Account ID", "Year", "Month", "Date", "Amount", "Status"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);

                java.util.Date date = transaction.getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                LocalDate transactionDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));


                String accountId = (transaction.getAccount() != null) ? transaction.getAccount().getIdAccount().toString() : "No Account";

                row.createCell(0).setCellValue(accountId);
                row.createCell(1).setCellValue(transactionDate.getYear());
                row.createCell(2).setCellValue(transactionDate.getMonthValue());
                row.createCell(3).setCellValue(date.toString());
                row.createCell(4).setCellValue(transaction.getAmount());
                row.createCell(5).setCellValue(transaction.getStatus().toString());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
