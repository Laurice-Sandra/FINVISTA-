package esprit.tn.flexifin.controllers;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.serviceImp.TransactionServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import esprit.tn.flexifin.entities.TranStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionRestController {
    TransactionServiceImp transactionService;

    JavaMailSender mailSender;


    //CRUD
    @PostMapping("/")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }


    @GetMapping("/")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable(value = "id") Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(transaction);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable(value = "id") Long transactionId,
                                                         @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionDetails);
        if (updatedTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTransaction);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(value = "id") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }

    //FILTER
    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> filterTransactions(
            @RequestParam(required = false) TranStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {

        List<Transaction> transactions = transactionService.filterTransactionsByStatusAndDate(status, date);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // sumByStatus
    @GetMapping("/sumByStatus")
    public ResponseEntity<Integer> sumTransactionsByStatus(@RequestParam TranStatus status) {
        return ResponseEntity.ok(transactionService.calculateSumByStatus(status));
    }

    // sumByDate
    @GetMapping("/sumByDate")
    public ResponseEntity<Integer> sumTransactionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return ResponseEntity.ok(transactionService.calculateSumByDate(date));
    }


    // TRANSACTIONS HISTORY
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Map<String, Object>>> getTransactionHistory(@PathVariable Long accountId) {
        List<Map<String, Object>> simplifiedHistory = transactionService.getSimplifiedTransactionHistory(accountId);
        return ResponseEntity.ok(simplifiedHistory);
    }

    //stripe
    @PostMapping("/create-payment-session")
    public ResponseEntity<String> createPaymentSession(@RequestParam Long accountId) {
        try {
            String sessionUrl = transactionService.createStripeSession(accountId);
            return ResponseEntity.ok(sessionUrl);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Stripe session: " + e.getMessage());
        }
    }

    @GetMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("session_id") String sessionId,
                                                 @RequestParam("account_id") Long accountId) {
        try {
            String message = transactionService.processPaymentSuccess(sessionId, accountId);
            return ResponseEntity.ok(message);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during the retrieval of the payment session: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error while recording the transaction or sending the email: " + e.getMessage());
        }
    }

    @GetMapping("/payment-cancelled")
    public ResponseEntity<String> paymentCancelled() {
        String message = transactionService.processPaymentCancelled();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestParam Long fromAccountId,
                                                @RequestParam Long toAccountId,
                                                @RequestParam Float amount) {
        try {
            String message = transactionService.transferFunds(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    //Excel

    @GetMapping("/report/flexible")
    public ResponseEntity<InputStreamResource> getFlexibleReport(
            @RequestParam(required = false) List<Long> accountIds,
            @RequestParam(required = false) List<Integer> years,
            @RequestParam(required = false) List<Integer> months) {

        try {
            ByteArrayInputStream in = transactionService.generateFlexibleReportExcel(accountIds, years, months);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Transactions-Report.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(in));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }




    /*ITransactionService iTransactionService;

    public List<Transaction> retrieveAllTransactions() {
        return iTransactionService.retrieveAllTransactions();
    }

    public Transaction addTransaction(Transaction transaction) {
        return iTransactionService.addTransaction(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return iTransactionService.updateTransaction(transaction);
    }

    public Transaction retrieveTransaction(Long idTransaction) {
        return iTransactionService.retrieveTransaction(idTransaction);
    }

    @PostMapping("/payments")
    public ResponseEntity<String> processPayment(@RequestBody Transaction paymentRequest) {
        try {
            Transaction transaction = iTransactionService.processPayment(paymentRequest);

            // Return a success response to the client
            return ResponseEntity.ok().body("Payment successful");
        } catch (StripeException e) {
            e.printStackTrace();

            // Return an error response to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment failed: " + e.getMessage());
        }

    }
*/
}
