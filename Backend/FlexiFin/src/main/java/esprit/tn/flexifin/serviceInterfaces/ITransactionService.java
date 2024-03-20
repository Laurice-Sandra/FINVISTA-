package esprit.tn.flexifin.serviceInterfaces;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Transaction;

import java.util.List;

public interface ITransactionService {
    List<Transaction> retrieveAllTransactions();

    Transaction addTransaction(Transaction transaction);

    Transaction updateTransaction(Transaction transaction);

    Transaction retrieveTransaction(Long idTransaction);

    Transaction processPayment(Transaction paymentRequest) throws StripeException;
}
