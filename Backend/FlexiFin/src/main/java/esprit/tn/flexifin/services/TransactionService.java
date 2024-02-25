package esprit.tn.flexifin.services;

import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;


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
        final Transaction updatedTransaction = transactionRepository.save(transaction);
        return updatedTransaction;
    }


    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found for this id :: " + transactionId));
        transactionRepository.delete(transaction);
    }

}
