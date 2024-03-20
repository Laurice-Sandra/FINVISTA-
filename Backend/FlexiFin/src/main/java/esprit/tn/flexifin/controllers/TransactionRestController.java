package esprit.tn.flexifin.controllers;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.serviceInterfaces.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
public class TransactionRestController {
    ITransactionService iTransactionService;

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
    @PostMapping("/processPayment/{sendId}/{receivId}")
    public ResponseEntity<String> processPayments(@RequestBody Transaction paymentRequest, @PathVariable("sendId") Long senderAccountId, @PathVariable("receivId") Long receiverAccountId) {
        try {
            Transaction transaction = iTransactionService.processPayments(paymentRequest,senderAccountId,receiverAccountId);

            // Return a success response to the client
            return ResponseEntity.ok().body("Payment successful");
        } catch (StripeException e) {
            e.printStackTrace();

            // Return an error response to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment failed: " + e.getMessage());
        }
    }
}
