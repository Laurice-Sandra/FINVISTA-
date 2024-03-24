package esprit.tn.flexifin.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.TranStatus;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        String customerEmail = "khalil.nacef.kn@gmail.com"; // For testing purpose

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(customerEmail);
        email.setFrom("khalil.nacef.kn@gmail.com");
        email.setSubject("Payment Confirmation");
        email.setText("Your payment has been successfully processed. Thank you for your purchase.");
        mailSender.send(email);

        recordTransaction(sessionId, accountId); // This method already exists in your service
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


}