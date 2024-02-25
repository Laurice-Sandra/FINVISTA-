package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {


    TransactionService transactionService;


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
}

