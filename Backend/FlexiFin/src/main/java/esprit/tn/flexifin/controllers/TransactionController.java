package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import esprit.tn.flexifin.entities.TranStatus;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {


    TransactionService transactionService;

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
        if(transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(transaction);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable(value = "id") Long transactionId,
                                                         @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionDetails);
        if(updatedTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTransaction);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(value = "id") Long transactionId){
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
        List<Transaction> history = transactionService.getTransactionHistory(accountId);
        List<Map<String, Object>> simplifiedHistory = history.stream().map(transaction -> {
            Map<String, Object> transactionInfo = new HashMap<>();
            transactionInfo.put("idTransaction", transaction.getIdTransaction());
            transactionInfo.put("amount", transaction.getAmount());
            transactionInfo.put("date", transaction.getDate());
            transactionInfo.put("status", transaction.getStatus());
            return transactionInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(simplifiedHistory);
    }
}

